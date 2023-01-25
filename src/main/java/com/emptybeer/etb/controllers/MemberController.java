package com.emptybeer.etb.controllers;

import com.emptybeer.etb.entities.member.EmailAuthEntity;
import com.emptybeer.etb.entities.member.UserEntity;
import com.emptybeer.etb.enums.CommonResult;
import com.emptybeer.etb.interfaces.IResult;
import com.emptybeer.etb.services.MemberService;
import com.emptybeer.etb.utils.CryptoUtils;
import org.apache.catalina.Context;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping(value = "/member")
public class MemberController {

    // 의존성 주입
    private final MemberService memberService;

    @Autowired
    //요구되는 타입을 스프링부트가 알아서 객체화 하여 전달토록 한다. (컨트롤러-서비스 간) 의존성 주입
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 로그인
    @GetMapping(value = "login",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getLogin() {
        ModelAndView modelAndView = new ModelAndView("member/login");
        return modelAndView;
    }

    @RequestMapping(value = "login",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)

    @ResponseBody
    public String postLogin(HttpSession session, UserEntity user) {
        Enum<?> result = this.memberService.login(user);
        if (result == CommonResult.SUCCESS) {
            session.setAttribute("user", user); // 해당 요소에 user 이름의 user 값을 가지는 HTML 속성을 추가한다.
            session.setAttribute("user", this.memberService.getUser(user.getEmail()));
            System.out.println("이메일/비밀번호 맞음.");
        } else {
            System.out.println("이메일/비밀번호 틀림.");
        }
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        return responseObject.toString();
    }


    //로그아웃
    @RequestMapping(value = "logout",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getLogout(HttpSession session) {
        session.setAttribute("user", null);
        ModelAndView modelAndView = new ModelAndView("redirect:login");
        return modelAndView;
    }


    // 회원가입
    @RequestMapping(value = "register",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    //맵핑, 주소값은 localhost:8080/member/register임, GET(주소창 입력) 방식으로 요청, 응답을 MediaType의 TEXT_HTML_VALUE로 돌려준다.
    public ModelAndView getRegister() {
        ModelAndView modelAndView = new ModelAndView("member/register");
        return modelAndView;
    }

    @RequestMapping(value = "register",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postRegister(UserEntity user, EmailAuthEntity emailAuth) throws NoSuchAlgorithmException {
        Enum<?> result = this.memberService.register(user, emailAuth);
        // 'MemberService' 가 가진 'register' 메서드에 'user' 및 'emailAuth' 전달하여 호출하기.
        JSONObject responseObject = new JSONObject();
        // <1>이 반환하는 결과 'Enum<?>'를 받아와 'JSONObject' 타입의 응답 결과 만들기.
        responseObject.put("result", result.name().toLowerCase());
        //"result" 자바스크립트에도 switch문에 확인
        if (result == CommonResult.SUCCESS) {
            responseObject.put("salt", emailAuth.getSalt());
        }
        return responseObject.toString();
        // 3. <2>에서 만들어진 'JSONObject' 객체를 문자열화(toString) 하여 반환하기.
    }

    // 마이페이지 맵핑
    @GetMapping(value = "myPage",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMyPage() {
        ModelAndView modelAndView = new ModelAndView("member/myPage");
        return modelAndView;
    }

    // 마이페이지 작성 글 보기 카테고리 맵핑
    @GetMapping(value = "myArticle",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMyArticle() {
        ModelAndView modelAndView = new ModelAndView("member/myArticle");
        return modelAndView;
    }

    // 마이페이지 작성 댓글 보기 카테고리 맵핑
    @GetMapping(value = "myComment",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMyComment() {
        ModelAndView modelAndView = new ModelAndView("member/myComment");
        return modelAndView;
    }

    // 마이페이지 좋아요한 글 보기 카테고리 맵핑
    @GetMapping(value = "myLike",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMyLike() {
        ModelAndView modelAndView = new ModelAndView("member/myLike");
        return modelAndView;
    }

    // 마이페이지 닉네임 변경 카테고리 맵핑
    @GetMapping(value = "changeNickname",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getChangeNickname() {
        ModelAndView modelAndView = new ModelAndView("member/changeNickname");
        return modelAndView;
    }

    // 마이페이지 연락처 변경 카테고리 맵핑
    @GetMapping(value = "changeContact",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getChangeContact() {
        ModelAndView modelAndView = new ModelAndView("member/changeContact");
        return modelAndView;
    }

    // 마이페이지 주소 변경 카테고리 맵핑
    @GetMapping(value = "changeAddress",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getChangeAddress() {
        ModelAndView modelAndView = new ModelAndView("member/changeAddress");
        return modelAndView;
    }

    // 이메일
    @RequestMapping(value = "email",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postEmail(UserEntity user, EmailAuthEntity emailAuth) throws NoSuchAlgorithmException, MessagingException {
        Enum<? extends IResult> result = this.memberService.sendEmailAuth(user, emailAuth);
        // ?만 적어줘도 됨.
        // 브라우저에게 salt 값을 넘겨줘야 되는데 user,까지 적어주면 솔트값을 못전해줌 emailAuth에서 솔트 지정해주면 그 값이 넘어오기 때문에 emailAuth를 꼭 써주어야 한다.
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        // result의 name 메서드를 불러와 소문자화
        if (result == CommonResult.SUCCESS) {
            responseObject.put("salt", emailAuth.getSalt());
        }
        //F12에 솔트값이 넘어옴. DB에도 찍힘
        return responseObject.toString();
        //  {"result":"success"} 를 브라우저가 반환
    }

    @RequestMapping(value = "email",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchEmail(EmailAuthEntity emailAuth) {
        Enum<?> result = this.memberService.verifyEmailAuth(emailAuth);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        return responseObject.toString();
    }

    // 비밀번호 재설정
    @RequestMapping(value = "recoverPassword",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getRecoverPassword() {
        ModelAndView modelAndView = new ModelAndView("member/recoverPassword");
        return modelAndView;
    }

    @RequestMapping(value = "recoverPassword",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody     // 중요
    public String postRecoverPassword(EmailAuthEntity emailAuth, HttpServletRequest request) throws MessagingException {
        Enum<?> result = this.memberService.recoverPasswordSend(emailAuth, request); // this.이 자리에 SUCCESS 또는 FAILURE 로 바뀌게 되고
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseObject.put("index", emailAuth.getIndex());
        }
        return responseObject.toString(); // "{"result":"success"}"
    }

    @RequestMapping(value = "recoverPassword",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchRecoverPassword(EmailAuthEntity emailAuth, UserEntity user) {
        Enum<?> result = this.memberService.recoverPassword(emailAuth, user);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        return responseObject.toString();
    }


    // 비밀번호 재설정 이메일
    @RequestMapping(value = "recoverPasswordEmail",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getRecoverPasswordEmail(EmailAuthEntity emailAuth) {
        Enum<?> result = this.memberService.recoverPasswordAuth(emailAuth);
        ModelAndView modelAndView = new ModelAndView("member/recoverPasswordEmail");
        modelAndView.addObject("result", result.name());
        return modelAndView;
    }

    @RequestMapping(value = "recoverPasswordEmail",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postRecoverPasswordEmail(EmailAuthEntity emailAuth) {
        Enum<?> result = this.memberService.recoverPasswordcheck(emailAuth);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseObject.put("code", emailAuth.getCode());
            responseObject.put("salt", emailAuth.getSalt());
        }
        return responseObject.toString();
    }

    // 비밀번호 재설정 인증
    @ResponseBody
    public JSONObject recoverPasswordAuth(EmailAuthEntity emailAuth) {
        Enum<? extends IResult> result = this.memberService.recoverPasswordAuth(emailAuth);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseObject.put("code", emailAuth.getCode());
            responseObject.put("salt", emailAuth.getSalt());
        }
        return null;
    }

    // 이메일 찾기
    @RequestMapping(value = "recoverEmail",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getRecoverEmail() {
        ModelAndView modelAndView = new ModelAndView("member/recoverEmail");
        return modelAndView;
    }

    @RequestMapping(value = "recoverEmail",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postRecoverEmail(UserEntity user) {
        Enum<?> result = this.memberService.recoverEmail(user);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseObject.put("email", user.getEmail());
        }
        return responseObject.toString();
    }

    // 회원탈퇴
    @GetMapping(value = "delete",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getDelete() {
        ModelAndView modelAndView = new ModelAndView("member/delete");
        return modelAndView;
    }

    @RequestMapping(value = "user",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteUser(@SessionAttribute(value = "user", required = false) UserEntity user,
                             @RequestParam(value = "email") String email, // 입력한 이메일
                             @RequestParam(value = "password") String password,
                             HttpSession session) { // 입력받은 패스워드
        Enum<?> result;
        System.out.println("check1 " + user.getEmail());
        System.out.println("check1 " + user.getPassword());
        System.out.println("check1 " + email);
        System.out.println("check1 " + password);
        if (user.getEmail().equals(email) && user.getPassword().equals(CryptoUtils.hashSha512(password))) {
//        if (user.getEmail().equals(email) && CryptoUtils.hashSha512(user.getPassword()).equals(CryptoUtils.hashSha512(password))) {
            result = this.memberService.deleteUser(user);
        } else {
            result = CommonResult.FAILURE;
        }
        // 입력받은 이메일과 유저의 이메일이 같고, 입력받은 해싱된 패스워드와 해싱된 유저의 패스워드가 같을 경우
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseObject.put("email", user.getEmail());
            responseObject.put("password", user.getPassword());
            session.setAttribute("user", null);
        }

        return responseObject.toString();
    }

    // 닉네임 수정
    @RequestMapping(value = "changeNickname",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchChangeNickname(@SessionAttribute(value = "user", required = false) UserEntity user, @RequestParam(value = "changeNickname") String changeNickname, HttpSession session) {
        Enum<?> result = this.memberService.changeNickname(user, changeNickname);
        System.out.println("넘어오나????");
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        //세션 null -> 다시 user
        session.setAttribute("user", this.memberService.getUser(user.getEmail()));// UserEntity 값.   user.getEmail() 기준으로 다시 유저정보 모두 들고 와서 session에 set
        System.out.println("con check " + this.memberService.getUser(user.getEmail()).getContact());
        return responseObject.toString();
    }

    // 연락처 수정
    @RequestMapping(value = "changeContact",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchChangeContact(@SessionAttribute(value = "user", required = false) UserEntity user,
                                     @RequestParam(value = "changeContact") String changeContact,
                                     HttpSession session) {
        Enum<?> result = this.memberService.changeContact(user, changeContact);
        System.out.println("넘어오나????");
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        //세션 null -> 다시 user
        session.setAttribute("user", this.memberService.getUser(user.getEmail()));// UserEntity 값.   user.getEmail() 기준으로 다시 유저정보 모두 들고 와서 session에 set
        System.out.println("con check " + this.memberService.getUser(user.getEmail()).getContact());
        return responseObject.toString();
    }

    // 주소 수정
    @RequestMapping(value = "changeAddress",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchChangeAddress(@SessionAttribute(value = "user", required = false) UserEntity user,
                                     @RequestParam(value = "changeAddressPostal") String changeAddressPostal,
                                     @RequestParam(value = "changeAddressPrimary") String changeAddressPrimary,
                                     @RequestParam(value = "changeAddressSecondary") String changeAddressSecondary,
                                     HttpSession session) {
        Enum<?> result = this.memberService.changeAddress(user, changeAddressPostal, changeAddressPrimary, changeAddressSecondary);
        System.out.println("주소 변경 컨트롤러로 넘어오나????");
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        //세션 null -> 다시 user
        session.setAttribute("user", this.memberService.getUser(user.getEmail()));// UserEntity 값.   user.getEmail() 기준으로 다시 유저정보 모두 들고 와서 session에 set
        return responseObject.toString();
    }


}
