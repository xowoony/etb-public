package com.emptybeer.etb.mappers;

import com.emptybeer.etb.entities.member.EmailAuthEntity;
import com.emptybeer.etb.entities.member.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    UserEntity selectUserByEmail(@Param(value = "email") String email);

    UserEntity selectUserByEmailPassword(@Param(value = "email") String email,
                                         @Param(value = "password") String password);

    UserEntity selectUserByNameContact(@Param(value = "name") String name,
                                       @Param(value = "contact") String contact);
}
