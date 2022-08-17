import React, { useState, createContext, useEffect } from "react";
import UserService from "services/UserService";
import { useCookies } from "react-cookie";
import { Navigate, useLocation, useNavigate } from "react-router-dom";
import Common from "utils/common";
import fetchConfig from "configs/fetchConfig";
export interface IUser {
  id: number;
  username: string;
  roles: string[];
  phone_number: string;
  full_name: string;
  avatar_url: string;
}

export interface ContextType {
  user: IUser;
}
export const UserContext = createContext<ContextType>({} as ContextType);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [user, setUser] = useState<IUser>();
  const value = { user };

  return <UserContext.Provider value={value}>{children}</UserContext.Provider>;
};

export const Guard = ({ children, allowedRoles }) => {
  const [cookies] = useCookies();
  useEffect(() => {
    if (!allowedRoles.includes(cookies?.role)) Common.showToast("Tài khoản không có quyền truy cập", "error");
  }, [cookies?.role]);

  if (allowedRoles.includes(cookies.role) && cookies.token) return children;

  return <Navigate to="/login" replace />;
};
