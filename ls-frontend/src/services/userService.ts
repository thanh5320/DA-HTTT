import urlsApi from "configs/urls";

export const UserService = {
  login: (username, password) => {
    return fetch(urlsApi.user.login, {
      method: "POST",
      body: JSON.stringify({ username, password }),
    }).then((res) => res.json());
  },
};
