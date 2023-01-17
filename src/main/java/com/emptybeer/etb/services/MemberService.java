package com.emptybeer.etb.services;

import com.emptybeer.etb.entities.member.EmailAuthEntity;
import com.emptybeer.etb.entities.member.UserEntity;
import com.emptybeer.etb.enums.CommonResult;
import com.emptybeer.etb.enums.member.RegisterResult;
import com.emptybeer.etb.enums.member.SendEmailAuthResult;
import com.emptybeer.etb.enums.member.VerifyEmailAuthResult;
import com.emptybeer.etb.interfaces.IResult;
import com.emptybeer.etb.mappers.IMemberMapper;
import com.emptybeer.etb.utils.CryptoUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service(value = "com.emptybeer.etb.services.MemberService")
public class MemberService {
    private final JavaMailSender mailSender;     //의존성 주입
    private final TemplateEngine templateEngine;    // 템플릿 엔진
    private final IMemberMapper memberMapper;


    @Autowired
    public MemberService(JavaMailSender mailSender, TemplateEngine templateEngine, IMemberMapper MemberMapper) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.memberMapper = MemberMapper;
    }

    // 이메일 인증
    @Transactional
    public Enum<? extends IResult> sendEmailAuth(UserEntity user, EmailAuthEntity emailAuth)
            throws NoSuchAlgorithmException, MessagingException {
        UserEntity existingUser = this.memberMapper.selectUserByEmail(user.getEmail());
        if (existingUser != null) {
            // 이미 사용중인 이메일이라면 (값이 있다면)
            return SendEmailAuthResult.EMAIL_DUPLICATED;
            // 이자리에서 메서드 실행이 끝난다.
            // 이메일이 복사되었다.
        }
        String authCode = RandomStringUtils.randomNumeric(6);
        // 테스트코드 (test디렉토리-> RandomTest)
        // 랜덤한 숫자 문자열 6자리를 돌려준다
        // apache.commons 의존성 추가 후 작성
        String authSalt = String.format("%s%s%f%f",
                user.getEmail(),
                authCode,
                Math.random(),
                Math.random());
        StringBuilder authSaltHashBuilder = new StringBuilder();
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(authSalt.getBytes(StandardCharsets.UTF_8));
        for (byte hashByte : md.digest()) {
            authSaltHashBuilder.append(String.format("%02x", hashByte));
        }
        authSalt = authSaltHashBuilder.toString();

        Date createdOn = new Date();
        Date expiresOn = DateUtils.addMinutes(createdOn, 5);


        emailAuth.setEmail(user.getEmail());
        emailAuth.setCode(authCode);
        emailAuth.setSalt(authSalt);
        emailAuth.setCreatedOn(createdOn);
        emailAuth.setExpiresOn(expiresOn);
        emailAuth.setExpired(false);

        if (this.memberMapper.insertEmailAuth(emailAuth) == 0) {
            return CommonResult.FAILURE;
        }


        Context context = new Context();
        context.setVariable("code", emailAuth.getCode());

        String text = this.templateEngine.process("member/registerEmailAuth", context);
        MimeMessage mail = this.mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, "UTF-8");
        helper.setFrom("xowoony@gmail.com");
        helper.setTo(user.getEmail());
        helper.setSubject("[잔이비어] 회원가입 인증 번호");
        helper.setText(text, true);
        this.mailSender.send(mail);

        return CommonResult.SUCCESS;

    }

    @Transactional
    public Enum<? extends IResult> verifyEmailAuth(EmailAuthEntity emailAuth) {
        EmailAuthEntity existingEmailAuth = this.memberMapper.selectEmailAuthByEmailCodeSalt(
                emailAuth.getEmail(),
                emailAuth.getCode(),
                emailAuth.getSalt());
        if (existingEmailAuth == null) {
            // 3개 중 한 개 이상이 잘못되었을 경우 즉 인증번호가 잘못되었을 경우
            return CommonResult.FAILURE;
        }
        // 5분 지났을 경우(만료)
        if (existingEmailAuth.getExpiresOn().compareTo(new Date()) < 0) {
            // getExpiresOn(만료시점) 에서  new Date 를 빼줬다고 생각하라.
            return VerifyEmailAuthResult.EXPIRED;
        }


        //expired 올바른 인증번호를 입력하고 누르면 True가 되어야만 한다.
        existingEmailAuth.setExpired(true);
        if (this.memberMapper.updateEmailAuth(existingEmailAuth) == 0) {
            // 영향을 받은 레코드 갯수가 0일 경우
            return CommonResult.FAILURE;
        }
        return CommonResult.SUCCESS;
    }

    // 회원가입
    public Enum<? extends IResult> register(UserEntity user, EmailAuthEntity emailAuth) {
        // 1. 'emailAuth'가 가진 'email', 'code', 'salt'값 기준으로 새로운 'EmailAuthEntity' SELECT 해서 가져오기
        EmailAuthEntity existingEmailAuth = this.memberMapper.selectEmailAuthByEmailCodeSalt(
                // 값 세개를 넘겨받는다.
                emailAuth.getEmail(),
                emailAuth.getCode(),
                emailAuth.getSalt());
        // 2. <1>에서 가져온 새로운 객체가 null 이거나 이가 가진 isExpired() 호출 결과가 false 인 경우 'RegisterResult.EMAIL_NOT_VERIFIED'를 결과로 반환하기. (없음으로 만들어야 함).

        if (existingEmailAuth == null || !existingEmailAuth.isExpired()) {
            // 이메일인증을 정상적으로 완료하지 않았을 때
            // RegisterResult enums만들어 주고 와야함.
            return RegisterResult.EMAIL_NOT_VERIFIED;
            // 거절.
        }
        user.setPassword(CryptoUtils.hashSha512(user.getPassword()));
        // 비밀번호 암호화 (해싱)
        // CryptoUtils에 따로 빼놓은 것을 불러온다. 비밀번호가 해싱된 채로 반환 된 것을.
        //3. 'user'객체를 'IMemberMapper' 객체의 'insertUser' 메서드 호출시 전달인자로 하여 INSERT 하기.
        // 4. <3> 의 결과가 0이면, 'CommonResult.FAILURE' 반환하기.
        if (this.memberMapper.insertUser(user) == 0) {
            // 뭔지 모르겠지만 인서트가 안되었을 경우
            return CommonResult.FAILURE;
            // 실패
        }
        // 5. 위 과정 전체를 거친 후 'CommonResult.SUCCESS' 반환하기.
        return CommonResult.SUCCESS;
    }

    // 비밀번호 재설정
    @Transactional
    // recoverPasswordSend 리펙터링
    public Enum<? extends IResult> recoverPasswordSend(EmailAuthEntity emailAuth, HttpServletRequest req) throws MessagingException {
        UserEntity existingUser = this.memberMapper.selectUserByEmail(emailAuth.getEmail());
        // user가 가지고 있는 Email값을 넣는다. 여기 user는 위 user랑 같다. 위 user는 컨트롤러가 준다.
        if (existingUser == null) {
            // 이미 사용중인 이메일이라면 (값이 없다면)
            return CommonResult.FAILURE;
            // 이자리에서 메서드 실행이 끝난다.
            // 이메일이 복사되었다.
            // SUCCESS의 경우
        }
        String authCode = RandomStringUtils.randomNumeric(6);
        String authSalt = String.format("%s%s%f%f",
                authCode,
                emailAuth.getEmail(),
                Math.random(),
                Math.random());

        authSalt = CryptoUtils.hashSha512(authSalt);
        Date createOn = new Date(); // 현재일시
        Date expiresOn = org.apache.commons.lang3.time.DateUtils.addMinutes(createOn, 5); // 5분 미래
        emailAuth.setCode(authCode);
        emailAuth.setSalt(authSalt);
        emailAuth.setCreatedOn(createOn);
        emailAuth.setExpiresOn(expiresOn);
        emailAuth.setExpired(false);
        if (this.memberMapper.insertEmailAuth(emailAuth) == 0) {
            return CommonResult.FAILURE;
        }

        Context context = new Context();
        context.setVariable("email", emailAuth.getEmail());
        context.setVariable("code", emailAuth.getCode());
        context.setVariable("salt", emailAuth.getSalt());


        // 서비스에서 html로 인증코드를 넘겨줘야 하는데 그게 Context가 하는 일이다.
        context.setVariable("code", emailAuth.getCode());
        context.setVariable("domain", String.format("%s://%s:%d",
                req.getScheme(),
                req.getServerName(),
                req.getServerPort()));

        String text = this.templateEngine.process("member/recoverPasswordEmailAuth", context);
        MimeMessage mail = this.mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, "UTF-8");
        helper.setFrom("xowoony@gmail.com");
        helper.setTo(emailAuth.getEmail());
        helper.setSubject("[잔이비어] 비밀번호 재설정 인증 링크");
        helper.setText(text, true);
        this.mailSender.send(mail);
        return CommonResult.SUCCESS;
        // 사용중인 이메일이 면 success.
    }

    public Enum<? extends IResult> recoverPasswordcheck(EmailAuthEntity emailAuth) {
        // Membermapper.xml select
        EmailAuthEntity existingEmailAuth = this.memberMapper.selectEmailAuthByIndex(emailAuth.getIndex());
        if (existingEmailAuth == null || !existingEmailAuth.isExpired()) {
            return CommonResult.FAILURE;
        }
        emailAuth.setCode(existingEmailAuth.getCode());
        emailAuth.setSalt(existingEmailAuth.getSalt());
        return CommonResult.SUCCESS;
    }


    @Transactional
    public Enum<? extends IResult> recoverPasswordAuth(EmailAuthEntity emailAuth) {
        EmailAuthEntity existingEmailAuth = this.memberMapper.selectEmailAuthByEmailCodeSalt(
                emailAuth.getEmail(),
                emailAuth.getCode(),
                emailAuth.getSalt());
        if (existingEmailAuth == null) {
            return CommonResult.FAILURE;
        }
        if (new Date().compareTo(existingEmailAuth.getExpiresOn()) > 0) {
            return CommonResult.FAILURE;
        }
        existingEmailAuth.setExpired(true);
        if (this.memberMapper.updateEmailAuth(existingEmailAuth) == 0) {
            return CommonResult.FAILURE;
        }
        return CommonResult.SUCCESS;
    }


    // 비밀번호 재설정
    @Transactional
    public Enum<? extends IResult> recoverPassword(EmailAuthEntity emailAuth, UserEntity user) {
        //
        EmailAuthEntity existingEmailAuth = this.memberMapper.selectEmailAuthByEmailCodeSalt(
                emailAuth.getEmail(),
                emailAuth.getCode(),
                emailAuth.getSalt());
        if (existingEmailAuth == null || !existingEmailAuth.isExpired()) {
            return CommonResult.FAILURE;
        }
        // 이메일 인증이 아직 안됐거나 인증 시간이 5분이 지나서 만료되었을 경우 실패를 반환한다.
        UserEntity existingUser = this.memberMapper.selectUserByEmail(existingEmailAuth.getEmail());
        // 이메일값을 기준으로 DB에서 셀렉트 해온 값.existingUser는 DB에서 왔다.
        existingUser.setPassword(CryptoUtils.hashSha512(user.getPassword()));
        // 새롭게 입력한 비밀번호로 해싱 후 비밀번호를 수정한다.
        if (this.memberMapper.updateUser(existingUser) == 0) {
            // 데이터를 싹다 가지고 와서 그중에 비밀번호만 바꾸고 다시 집어넣는 과정이다.
            return CommonResult.FAILURE;
        }
        return CommonResult.SUCCESS;
    }

    // 로그인
    @Transactional
    public Enum<? extends IResult> login(UserEntity user) {
        UserEntity existingUser = this.memberMapper.selectUserByEmailPassword(
                user.getEmail(),
                CryptoUtils.hashSha512(user.getPassword()));
        if (existingUser == null) {
            System.out.println("실패!!!!");
            return CommonResult.FAILURE;
        }
        System.out.println("성공!!!!");
        user.setNickname(existingUser.getNickname());
        return CommonResult.SUCCESS;
    }

    // 이메일 찾기
    @Transactional
    public Enum<? extends IResult> recoverEmail(UserEntity user) {
        UserEntity findEmail = this.memberMapper.selectUserByNameContact(
                user.getName(),
                user.getContact());
        if (findEmail == null) {
            return CommonResult.FAILURE;
        }
        user.setEmail(findEmail.getEmail());
        return CommonResult.SUCCESS;
    }


    // 회원 탈퇴
    public Enum<? extends IResult> deleteUser(UserEntity user) {
        int existingUser = this.memberMapper.deleteUser(user);
        return CommonResult.SUCCESS;
    }


    // 닉네임 변경
    @Transactional
    public Enum<? extends IResult>changeNickname(UserEntity user, String changeNickname) {
        UserEntity existingUser = this.memberMapper.selectUserByEmail(user.getEmail());
        existingUser.setNickname(changeNickname);
        return this.memberMapper.updateUser(existingUser) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    // 연락처 변경
    @Transactional
    public Enum<? extends IResult>changeContact(UserEntity user, String changeContact) {
        UserEntity existingUser = this.memberMapper.selectUserByEmail(user.getEmail());
        existingUser.setContact(changeContact);

        return this.memberMapper.updateUser(existingUser) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    // 주소 변경
    @Transactional
    public Enum<? extends IResult>changeAddress(UserEntity user,
                                                String changeAddressPostal,
                                                String changeAddressPrimary,
                                                String changeAddressSecondary) {
        UserEntity existingUser = this.memberMapper.selectUserByEmail(user.getEmail());
        existingUser.setAddressPostal(changeAddressPostal);
        existingUser.setAddressPrimary(changeAddressPrimary);
        existingUser.setAddressSecondary(changeAddressSecondary);
        return this.memberMapper.updateUser(existingUser) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }
    
    // 유저 정보 다 불러오기
    public UserEntity getUser(String email) {
        return this.memberMapper.selectUserByEmail(email);
    }
}
