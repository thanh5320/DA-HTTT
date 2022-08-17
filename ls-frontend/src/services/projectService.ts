import urlsApi from "configs/urls";

export const ProjectService = {
  all: () => {
    return fetch(urlsApi.project.all, {
      method: "GET",
    }).then((res) => res.json());
  },
  create: (data) => {
    return fetch(urlsApi.project.create, {
      method: "POST",
      body: JSON.stringify(data),
    }).then((res) => res.json());
  },
  delete: (id) => {
    return fetch(urlsApi.project.delete + `/${id}`, {
      method: "DELETE",
    }).then((res) => res.json());
  },
  update: (data) => {
    return fetch(urlsApi.project.update, {
      method: "PUT",
      body: JSON.stringify(data),
    }).then((res) => res.json());
  },
};
