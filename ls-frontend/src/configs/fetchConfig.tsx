import fetchIntercept from "fetch-intercept";
import { useCookies } from "react-cookie";
export default () => {
  const [cookies] = useCookies();
  return fetchIntercept.register({
    request(url, config) {
      if (config == null) {
        config = {};
      }
      if (config.headers == null) {
        config.headers = {};
      }
      if (cookies.token) {
        config.headers["Authorization"] = `Bearer ${cookies.token}`;
      }
      if (config.headers.Accept == null) {
        config.headers.Accept = "application/json";
      }
      if (config.headers["Content-Type"] == null) {
        config.headers["Content-Type"] = "application/json";
      }
      if (!url.startsWith("http")) {
        if (!url.startsWith("/")) {
          url = `/${url}`;
        }
        if (url.indexOf(".hot-update.json") === -1) {
          url = process.env.APP_API_URL + url;
        }
      }
      return [url, config];
    },

    requestError(error) {
      return Promise.reject(error);
    },

    response(response) {
      if (response.status === 500) throw new Error("Lỗi hệ thống");
      return response;
    },

    responseError(error) {
      return Promise.reject(error);
    },
  });
};
