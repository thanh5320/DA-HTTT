import Icon from "components/icon";
import Loading from "components/Loading/Loading";
import moment from "moment";
import React from "react";
import { useQuery } from "react-query";
import { Modal } from "reactstrap";
import { NewsService } from "services/newsService";

const NewsDetail = ({ modal, toggle, item }) => {
  const { data, isLoading } = useQuery(
    ["fetchNewsDetail", { item }],
    () => NewsService.view(item.id, item.index_name),
    {
      onSuccess: (data) => {
        console.log(data);
      },
    }
  );
  if (isLoading)
    return (
      <Modal isOpen={modal} toggle={toggle} backdrop={true} scrollable={true}>
        <Loading size={10} />
      </Modal>
    );
  return (
    <Modal isOpen={modal} toggle={toggle} backdrop={true} scrollable={true}>
      <div className="container-news-detail">
        <div className="container-news-detail__header">
          <h4>Chi tiết tin bài</h4>
          <Icon name="Xmark" onClick={() => toggle(false)} />
        </div>
        <div className="container-news-detail__body">
          <h1>{item.title}</h1>
          <div className="container-news-detail__body-info">
            <div className="container-news-detail__body-info--left">
              <h5>
                <Icon name="CaretDown" /> Thông tin chung
              </h5>
              <p>
                Thời gian đăng: <span>{moment(item.published_time).format("DD/MM/YYYY")}</span>{" "}
              </p>
              <p>
                Thời gian thu thập: <span>{moment(item.first_crawled_time).format("DD/MM/YYYY")}</span>{" "}
              </p>
              <p>
                Nguồn: <span>{item.domain}</span>{" "}
              </p>
              <p>
                Tác giả: <span>{item.author_display_name}</span>
              </p>
            </div>
            <div className={`container-news-detail__body-info--right`}>
              <p>
                Sắc thái:
                <span className={`sentiment${item.sentiment}`}>
                  {item.sentiment === 0 ? "Trung lập" : item.sentiment === 1 ? "Tích cực" : "Tiêu cực"}
                </span>
              </p>
            </div>
          </div>

          <div className="container-news-detail__body-content">
            <div className="container-news-detail__body-content__info">
              <div className="container-news-detail__body-content__info-field">
                <span>Tóm tắt</span>
                <p>{item.summary}</p>
              </div>
              <div className="container-news-detail__body-content__info-field">
                <span>Từ khóa</span>
                <p className="container-news-detail__body-content__info-field__tags">
                  {item.tag.map((el, index) => (
                    <span key={index}>{el}</span>
                  ))}
                </p>
              </div>
            </div>
            <p>{item.content}</p>
            <p>{data.result}</p>
          </div>
        </div>
      </div>
    </Modal>
  );
};

export default NewsDetail;
