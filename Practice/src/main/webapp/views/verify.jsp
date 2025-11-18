<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="container">
    <br/>
    <br/>
    <br/>
    <span>Hệ thống đã gửi mã kích hoạt đến Email của bạn.</span>
    <span>Xin vui lòng kiểm tra Email để lấy mã kích hoạt tài khoản của bạn.</span>
    <br/>
    <div>
        <form action="VerifyCode" method="post" class="log-reg-block sky-form">
            <div class="input-group">
                <input type="text" name="authcode" class="form-control margin-top-20">
            </div>
            <input type="submit" value="Kích hoạt" class="btn-u btn-u-sea-shop margin-top-20">
        </form>
    </div>
    <br/>
    <br/>
    <br/>
</div>