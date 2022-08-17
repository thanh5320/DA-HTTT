import "animate.css";
import React, { useEffect } from "react";
import { Route, Routes, useNavigate } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "styles/main.scss";
import fetchConfig from "./configs/fetchConfig";
import { QueryClient, QueryClientProvider } from "react-query";
import Login from "pages/login";
import Layout from "pages/layout";
import { useCookies } from "react-cookie";

export const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
    },
  },
});

export default function App() {
  const [cookies] = useCookies();
  const navigate = useNavigate();
  fetchConfig();
  useEffect(() => {
    if (!cookies.token) navigate("/login");
  }, []);
  return (
    <QueryClientProvider client={queryClient}>
      <ToastContainer
        position="top-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="colored"
      />
      <Routes>
        <Route path="*" element={<Layout />} />
        <Route path="/login" element={<Login />} />
      </Routes>
    </QueryClientProvider>
  );
}
