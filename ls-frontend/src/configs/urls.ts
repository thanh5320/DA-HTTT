const urlsApi = {
  user: {
    login: "/auth/login",
  },
  project: {
    detail: "/project/detail",
    create: "/project/create",
    delete: "/project/delete",
    update: "/project/update",
    all: "/project/all",
  },
  chart: {
    //loại biểu đồ: đường, tròn, cột,
    getType: "/chart/chart-type",
    //sắc thái, lượt nhắc tới, phân bố sắc thái
    getCategory: "/chart/chart-category",
    create: "/chart/create",
    update: "/chart/update",
    delete: "/chart/delete",
    stats: "/chart/stats",
  },
  dashboard: {
    create: "/dashboard/create",
    update: "/dashboard/update",
    delete: "/dashboard/delete",
    detail: "/dashboard/detail",
    all: "/dashboard/all",
  },
  news: {
    view: "/news/view-article-html",
    search: "/news/search",
  },
};

export default urlsApi;
