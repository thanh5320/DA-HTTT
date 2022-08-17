import Loading from "components/Loading/Loading";
import { Pagination } from "components/pagination/pagination";
import SearchTable from "components/searchTable/SearchTable";
import Tags from "components/tags/Tags";
import { isError } from "lodash";
import moment from "moment";
import React, { useState } from "react";
import { useQuery } from "react-query";
import { NewsService } from "services/newsService";
import Common from "utils/common";
import "./index.scss";
import NewsDetail from "./partials/NewsDetail";

const News = () => {
  const [filter, setFilter] = useState({
    keywords: [],
    date_from: moment().add(-30, "day").format("YYYY/MM/DD 00:00:00"),
    date_to: moment().format("YYYY/MM/DD 23:59:59"),
    sentiments: [],
  });

  const [itemSelected, setItemSelected] = useState(null);
  const [modalDetail, setModalDetail] = useState(false);
  const [page, setPage] = useState(0)

  const { data, isLoading, isError } = useQuery(
    ["fetchNews", { filter, page }],
    ({signal}) => NewsService.all(filter.date_from, filter.date_to, filter.keywords, filter.sentiments, page, 12, signal),
    {
      onSuccess: (data) => {
        if (data.code === 0) {
          //console.log(data.result);
        } else {
          Common.showToast("Vui lòng thử lại", "error");
        }
      },
    }
  );
  return (
    <div className="container-news">
      {modalDetail && itemSelected && (
        <NewsDetail toggle={() => setModalDetail(!modalDetail)} modal={modalDetail} item={itemSelected} />
      )}
      <header>Tin bài</header>
      <SearchTable
        list={[
          {
            title: "Sắc thái",
            type: "select",
            options: [
              {
                label: "Tiêu cực",
                value: "-1",
              },
              {
                label: "Trung lập",
                value: "0",
              },
              {
                label: "Tích cực",
                value: "1",
              },
            ],
          },
          {
            title: "Ngày",
            type: "date",
          },
        ]}
        onChangeTable={(table) => {
          const { tags, date } = table;
          const sentiments = tags.filter((el) => el.label === "Sắc thái").map((el) => el.selected.value);
          setFilter({
            ...filter,
            keywords: table.search ? [table.search] : [],
            sentiments: sentiments ? sentiments : filter.sentiments,
            date_from: date[0] ? moment(date[0]).format("YYYY/MM/DD 00:00:00") : filter.date_from,
            date_to: date[1] ? moment(date[1]).format("YYYY/MM/DD 23:59:59") : filter.date_to,
          });
          setPage(0);
        }}
      />

      {isLoading ? (
        <Loading size={10} />
      ) : isError ? (
        "Có lỗi vui lòng thửr lại"
      ) : data.result ? (
        <>
        <div className="container-news__list">
          {data?.result.hits.map((el, index) => (
            <div
              className="container-news__list-item"
              onClick={() => {
                setModalDetail(!modalDetail);
                setItemSelected(el);
              }}
              key={index}
            >
              <div className="container-news__list-item__header">
                <div className="container-news__list-item__header-title">
                  <p>{el.domain}</p>
                  <span>Ngày đăng: {moment(el.published_time).format("DD/MM/YYYY")}</span>
                </div>
                <div className={`container-news__list-item__header-sentiment sentiment${el.sentiment}`}>
                  {el.sentiment === 0 ? "Trung lập" : el.sentiment === 1 ? "Tích cực" : "Tiêu cực"}
                </div>
              </div>
              <div className="container-news__list-item__body">
                <h3 className="container-news__list-item__body-title">{el.title}</h3>
                <div className="container-news__list-item__body-tags">
                  {el.tag.map((t, ids) => (
                    <span key={ids}>{t}</span>
                  ))}
                </div>
                <div className="container-news__list-item__body-summary">{el.summary}</div>
                {/* <div className="container-news__list-item__body-content">{el.content}</div> */}
              </div>
            </div>
          ))}
        </div>
        <Pagination
            name="tin bài"
            displayNumber={5}
            page={page + 1}
            setPage={(page) => setPage(page - 1)}
            sizeLimit={12}
            totalItem={data.result.total}
            totalPage={Math.ceil(data.result.total / 12)}
          /></>
      ) : (
        "không có tin bài phù hợp"
      )}
    </div>
  );
};

export default News;
