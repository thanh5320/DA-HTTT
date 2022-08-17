import urlsApi from "configs/urls";

export const DashBoardService = {
  all: () => {
    return fetch(urlsApi.dashboard.all, {
      method: "GET",
    }).then((res) => res.json());
  },
  detail: (id) => {
    return fetch(urlsApi.dashboard.detail + `/${id}`, {
      method: "GET",
    }).then((res) => res.json());
  },
  create: (name) => {
    return fetch(urlsApi.dashboard.create, {
      method: "POST",
      body: JSON.stringify({ name }),
    }).then((res) => res.json());
  },
  update: (id, name) => {
    return fetch(urlsApi.dashboard.update, {
      method: "put",
      body: JSON.stringify({ id, name }),
    }).then((res) => res.json());
  },
  delete: (id) => {
    return fetch(urlsApi.dashboard.delete + `/${id}`, {
      method: "delete",
    }).then((res) => res.json());
  },
};
