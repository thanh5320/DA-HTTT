import urlsApi from "configs/urls";

export const NewsService = {
  all: (date_from, date_to, keywords, sentiments, page, page_size, signal) => {
    return fetch(urlsApi.news.search, {
      method: "post",
      body: JSON.stringify({
        date_from,
        date_to,
        sentiments,
        keywords,
        page,
        size: page_size
      }),
      signal
    }).then((res) => res.json());
  },
  view: (article_id, index_name) => {
    return fetch(urlsApi.news.view, {
      method: "post",
      body: JSON.stringify({
        article_id,
        index_name,
      }),
    }).then((res) => res.json());
  },
};
