import Logo from "assets/images/logo.png";
import Icon from "components/icon";
import Popover from "components/popover/popover";
import SelectCustom from "components/selectCustom/selectCustom";
import React, { useEffect, useRef, useState } from "react";
import { useCookies } from "react-cookie";
import { Link, useNavigate } from "react-router-dom";
import Function from "utils/function";
import "./Header.scss";

export default function Header() {
  const [cookies, setCookie, removeCookie] = useCookies();
  return (
    <div className="base-header">
      <Link to="/" className="base-header-logo base-header-navigation-item">
        listening social
      </Link>
      <div className="base-header-navigation">
        <Link className="base-header-navigation-item" to="">
          Tổng quan
        </Link>
        <Link className="base-header-navigation-item" to="/project">
          Bộ truy vấn
        </Link>
        <Link className="base-header-navigation-item" to="/news">
          Tin bài
        </Link>
      </div>
      <Link
        to="/login"
        onClick={() => removeCookie("token")}
        className="base-header-navigation-item base-header__logout"
      >
        Đăng xuất
      </Link>
    </div>
  );
}

export const HeaderBottom = () => {
  return (
    <div className="header-bottom">
      <div className="header-bottom__container">
        <div className="header-bottom__left">
          <SelectCustom
            placeholder="Chọn khoảng thời gian"
            options={[
              { value: "1", label: "Tuần này" },
              { value: "2", label: "Tuần trước" },
              { value: "3", label: "15 ngày trước" },
            ]}
          />
        </div>
        <div className="header-bottom__right">
          <span className="header-bottom__label">Tải xuống file báo cáo:</span>
          <button className="header-bottom__btn">
            <Icon name="Download" />
            Xuất file ảnh
          </button>
          <button className="header-bottom__btn">
            <Icon name="Download" />
            Xuất file pdf
          </button>
        </div>
      </div>
    </div>
  );
};
