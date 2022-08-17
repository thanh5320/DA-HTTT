import urlsApi from "configs/urls";

export const ChartService = {
  stats: (date_from, date_to, chart_category_id, project_ids) => {
    return fetch(urlsApi.chart.stats, {
      method: "POST",
      body: JSON.stringify({ date_from, date_to, chart_category_id, project_ids }),
    }).then((res) => res.json());
  },
  getType: () => {
    return fetch(urlsApi.chart.getType, {
      method: "get",
    }).then((res) => res.json());
  },
  getCategory: () => {
    return fetch(urlsApi.chart.getCategory, {
      method: "get",
    }).then((res) => res.json());
  },
  create: (name, chart_type_id, chart_category_id, dashboard_id, project_ids) => {
    return fetch(urlsApi.chart.create, {
      method: "post",
      body: JSON.stringify({ name, chart_type_id, chart_category_id, dashboard_id, project_ids }),
    }).then((res) => res.json());
  },
  update: (id, name, chart_type_id, chart_category_id, project_ids) => {
    return fetch(urlsApi.chart.update, {
      method: "post",
      body: JSON.stringify({ id, name, chart_type_id, chart_category_id, project_ids }),
    }).then((res) => res.json());
  },
  delete: (id) => {
    return fetch(urlsApi.chart.delete + `/${id}`, {
      method: "delete",
    }).then((res) => res.json());
  },
};
