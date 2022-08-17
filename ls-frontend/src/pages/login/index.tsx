import React, { useState } from "react";
import { useMutation } from "react-query";
import { useNavigate } from "react-router-dom";
import { useCookies } from "react-cookie";
import { UserService } from "services/userService";
import Common from "utils/common";
import * as Yup from "yup";
import { Controller, useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import Input from "components/input/input";
import Button from "components/button/button";
import BeatLoader from "react-spinners/BeatLoader";
import "./index.scss";
import bg from "assets/images/bg-thanh.jpg";

const validationSchema = Yup.object().shape({
  username: Yup.string()
    .required("Tên đăng nhập không được để trống")
    .matches(/^[a-zA-Z0-9]*$/, "Tên đăng nhập chỉ được phép nhập chữ thường và số.")
    .max(50, "Tên đăng nhập không được quá 50 ký tự.")
    .min(1, "Tên đăng nhập tối thiểu 8 ký tự."),
  password: Yup.string()
    .min(1, "Mật khẩu tối thiểu 8 kí tự")
    .required("Mật khẩu không được để trống")
    .max(256, "Mật khẩu nhập tối đa 50 ký tự."),
});

const Login = () => {
  const {
    handleSubmit,
    control,
    setValue,
    formState: { errors, isDirty, isValid },
  } = useForm({ resolver: yupResolver(validationSchema), mode: "onChange" });

  const [cookies, setCookie] = useCookies();

  const navigate = useNavigate();

  const login = useMutation(
    (data: any) => {
      return UserService.login(data.username, data.password);
    },
    {
      onSuccess: (data) => {
        if (data.code === 0) {
          setCookie("token", data.result.token);
          navigate("/");
        } else {
          Common.showToast("Looix vui long thu laij", "error");
        }
      },
    }
  );
  return (
    <div className="login">
      <div className="login__bg">
        {/* <img src={bg} alt="" /> */}
      </div>
      <div className="login__form">
        <div className="login__form__container">
          <h3 className="login__form__title">Đăng nhập</h3>
          <span className="login__form__subtitle">Nhập tên đăng nhập và mật khẩu của bạn để sử dụng Alert.vn</span>

          <form
            onSubmit={handleSubmit((data) => {
              login.mutateAsync(data);
            })}
            className="login__form__form"
          >
            <Controller
              control={control}
              name="username"
              render={({ field: { onChange, value, onBlur } }) => (
                <Input
                  value={value}
                  className="input-field"
                  label="Tên đăng nhập"
                  placeholder="Nhập tên đăng nhập"
                  error={!!errors.username}
                  message={errors?.username?.message}
                  onChange={onChange}
                  onBlur={(e) => {
                    setValue("username", value.replace(/\s+/g, " ").trim());
                    onBlur();
                  }}
                />
              )}
            />
            <Controller
              name="password"
              control={control}
              render={({ field: { onChange, value, onBlur } }) => (
                <Input
                  className="input-field"
                  type="password"
                  label="Mật khẩu"
                  placeholder="Nhập mật khẩu"
                  value={value}
                  onChange={onChange}
                  onBlur={(e) => {
                    setValue("password", e.target.value.replace(/\s+/g, " ").trim());
                    onBlur();
                  }}
                  error={!!errors.password}
                  message={errors?.password?.message}
                />
              )}
            />

            <div className="form-btn">
              <Button type="submit" disabled={!isValid || login.isLoading} color="primary">
                Đăng nhập {login.isLoading ? <BeatLoader color={"#1850A8"} loading={true} size={10} /> : null}
              </Button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Login;
