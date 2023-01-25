package com.emptybeer.etb.mappers;

import com.emptybeer.etb.entities.member.EmailAuthEntity;
import com.emptybeer.etb.entities.member.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpSession;

@Mapper
public interface IMemberMapper {
    int insertEmailAuth(EmailAuthEntity emailAuthEntity);
    int updateEmailAuth(EmailAuthEntity emailAuth);

    int insertUser(UserEntity user);

    int updateUser(UserEntity user);




    EmailAuthEntity selectEmailAuthByEmailCodeSalt(@Param(value = "email") String email,
                                                   @Param(value = "code") String code,
                                                   @Param(value = "salt") String salt);

    EmailAuthEntity selectEmailAuthByIndex(@Param(value = "index") int index);

    // 회원탈퇴, 닉네임 변경
    UserEntity selectUserByEmail(@Param(value = "email") String email);

    // 닉네임 변경
//    UserEntity selectUserByNickname(@Param(value = "nickname") String nickname);

    UserEntity selectUserByEmailPassword(@Param(value = "email") String email,
                                         @Param(value = "password") String password);

    UserEntity selectUserByNameContact(@Param(value = "name") String name,
                                       @Param(value = "contact") String contact);

//    UserEntity selectUserNickname(@Param(value="nickname") String nickname);

    // 회원탈퇴
    int deleteUser(UserEntity user);

}
