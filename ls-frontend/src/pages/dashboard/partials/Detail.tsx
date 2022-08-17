import { queryClient } from "App";
import Button from "components/button/button";
import Dialog from "components/dialog/dialog";
import React, { useState } from "react";
import { useMutation, useQueries, useQuery } from "react-query";
import { useParams } from "react-router-dom";
import { ChartService } from "services/chartService";
import { DashBoardService } from "services/dashboardService";
import Common from "utils/common";
import UpdateChart from "../UpdateChart";
import CreateChart from "./CreateChart";

import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  PointElement,
  LineElement,
  Filler,
  ArcElement,
} from "chart.js";
import { Bar, Line, Pie } from "react-chartjs-2";
import Icon from "components/icon";
import DoubleDatePicker from "components/DoubleDatePicker/DoubleDatePicker";
import moment from "moment";
import Loading from "components/Loading/Loading";
import Popover from "components/popover/popover";
import { after } from "lodash";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  LineElement,
  ArcElement,
  PointElement,
  Title,
  Tooltip,
  Legend,
  Filler
);

export const optionsBar = {
  plugins: {
    title: {
      display: true,
    },
  },
  responsive: true,
  scales: {
    x: {
      stacked: true,
    },
    y: {
      stacked: true,
    },
  },
};

export const optionsLine = {
  responsive: true,
  plugins: {
    legend: {
      position: "top" as const,
    },
    title: {
      display: true,
    },
  },
};

export const optionsBarHidLegend = {
  plugins: {
    title: {
      display: true,
    },
    legend: {
      display: false,
    },
  },
  responsive: true,
  scales: {
    x: {
      stacked: true,
    },
    y: {
      stacked: true,
    },
  },
};

export const optionsLineHideLegend = {
  responsive: true,
  plugins: {
    legend: {
      display: false
    },
    title: {
      display: true,
    },
  },
};

const Charts = ({ charts, dates }) => {
  const chartQueries = useQueries(
    charts.map((chart) => {
      const chart_category_id = chart.chart_category.id ? chart.chart_category.id : chart.chart_category;
      return {
        queryKey: ["chart", { chart, dates }],
        queryFn: () =>
          ChartService.stats(
            moment(dates[0]).format("YYYY/MM/DD 00:00:00"),
            moment(dates[1]).format("YYYY/MM/DD 23:59:59"),
            chart_category_id,
            chart.project_ids
          ),
      };
    })
  );
  const [modalUpdateChart, setModalUpdateChart] = useState(false);
  const [modalDeleteChart, setModalDeleteChart] = useState(false);

  const [itemSelected, setItemSelected] = useState(null);

  const deleteChartMutation = useMutation(
    (data) => {
      return ChartService.delete(data);
    },
    {
      onSuccess: (data) => {
        if (data.code === 0) {
          Common.showToast("Xóa biểu đồ thành công", "success");
        } else {
          Common.showToast("Xóa biểu đồ không thành công", "error");
        }
      },
    }
  );
  return (
    <div className="container-charts">
      {modalDeleteChart && itemSelected && (
        <Dialog
          title="Xác nhận xóa biều đồ"
          message={`Bạn có chắn chắn muốn xóa biều đồ ${itemSelected.name}`}
          modal={modalDeleteChart}
          toggle={(d) => {
            if (d) deleteChartMutation.mutate(itemSelected.id);
            setModalDeleteChart(!modalDeleteChart);
          }}
        />
      )}
      {modalUpdateChart && itemSelected ? (
        <UpdateChart
          modal={modalUpdateChart}
          toggle={(d) => {
            if (d) {
              queryClient.invalidateQueries(["fetchDetailDashboard"], {
                refetchActive: true,
                refetchInactive: false,
              });
            }
            setModalUpdateChart(!modalUpdateChart);
          }}
          data={itemSelected}
        />
      ) : null}
      <div className="container-charts-charts">
        {charts.map((chart, index) => (
          <div key={index} className="container-charts-charts__item">
            <div className="container-charts-charts__item-header">
              <p>{chart.name}</p>
              <div className="container-charts-charts__item-actions">
                <span
                  onClick={() => {
                    setModalUpdateChart(!modalUpdateChart);
                    setItemSelected({
                      name: chart.name,
                      id: chart.id,
                      chart_type_id: chart.chart_type.id,
                      chart_category_id: chart.chart_category.id,
                      project_ids: chart.project_ids,
                    });
                  }}
                >
                  <Icon name="Edit" />
                  sửa
                </span>
                <span
                  onClick={() => {
                    setModalDeleteChart(!modalDeleteChart);
                    setItemSelected({
                      name: chart.name,
                      id: chart.id,
                      chart_type_id: chart.chart_type.id,
                      chart_category_id: chart.chart_category.id,
                      project_ids: chart.project_ids,
                    });
                  }}
                >
                  <Icon name="Delete" />
                  xóa
                </span>
              </div>
            </div>
            <div className="">
              {(() => {
                if (chartQueries[index].isLoading) {
                  return <Loading size={5} />;
                } else {
                  const chart_category_id = chart.chart_category.id ? chart.chart_category.id : chart.chart_category;
                  const before = chartQueries[index].data?.result;
                  let labels = before.map((el) => moment(el.date).format("MM/DD/YYYY"));
                  let neutral_count = before.map((el) => el.neutral_count);
                  let positive_count = before.map((el) => el.positive_count);
                  let negative_count = before.map((el) => el.negative_count);
                  if (before[0].data) {
                    const after = before.map((el) => {
                      const neutral_count = el.data
                        .map((t) => t.neutral_count)
                        .reduce((previousValue, currentValue) => previousValue + currentValue, 0);
                      const positive_count = el.data
                        .map((t) => t.positive_count)
                        .reduce((previousValue, currentValue) => previousValue + currentValue, 0);

                      const negative_count = el.data
                        .map((t) => t.negative_count)
                        .reduce((previousValue, currentValue) => previousValue + currentValue, 0);

                      return {
                        date: el.date,
                        neutral_count,
                        positive_count,
                        negative_count,
                      };
                    });

                    labels = after.map((el) => moment(el.date).format("MM/DD/YYYY"));
                    neutral_count = after.map((el) => el.neutral_count);
                    positive_count = after.map((el) => el.positive_count);
                    negative_count = after.map((el) => el.negative_count);
                  }
                  let dataChart = {};
                  if(chart_category_id !== 1) {
                    dataChart = {
                      labels,
                      datasets: [
                        {
                          label: "Tiêu cực",
                          data: negative_count,
                          backgroundColor: "rgb(255, 99, 132)",
                          borderColor: "rgb(255, 99, 132)",
                        },
                        {
                          label: "Tích cực",
                          data: positive_count,
                          backgroundColor: "rgb(75, 192, 192)",
                          borderColor: "rgb(75, 192, 192)",
                        },
                        {
                          label: "Trung lập",
                          data: neutral_count,
                          backgroundColor: "rgb(53, 162, 235)",
                          borderColor: "rgb(75, 192, 192)",
                        },
                      ],
                    };
                  }
                  else {
                    dataChart = {
                      labels,
                      datasets: [
                        {
                          label: "tin tức",
                          data: before.map(el => el.data[0].total_count),
                          backgroundColor: "rgb(75, 192, 192)",
                          borderColor: "rgb(75, 192, 192)",
                        }
                      ],
                    }
                  }
                  
                  


                  if (chart.chart_type.id === 1) {
                    return <Bar options={ chart_category_id === 1 ? optionsBarHidLegend : optionsBar} data={dataChart} />;
                  } else if (chart.chart_type.id === 2) {
                    return <Line options={ chart_category_id === 1 ? optionsLineHideLegend : optionsLine} data={dataChart} />;
                  } else if (chart.chart_type.id === 3) {
                    return (
                      <Pie
                        data={{
                          labels,
                          datasets: [
                            {
                              label: "tin tức",
                              data: chart_category_id !== 1 ? chartQueries[index].data?.result.map((el) => el.total_count) : chartQueries[index].data?.result.map((el) => el.data[0].total_count),
                              backgroundColor: chartQueries[index].data?.result.map(
                                (el, index) => `rgba(54, 162, 235, ${index * 0.2})`
                              ),
                              borderWidth: 1,
                            },
                          ],
                        }}
                      />
                    );
                  } else {
                    
                    return (
                      <Line
                        options={chart_category_id === 1 ? optionsLineHideLegend : optionsLine}
                        data={{
                          labels,
                          datasets: chart_category_id !== 1 ?  [
                            {
                              fill: true,
                              label: "Tiêu cực",
                              data: negative_count,
                              backgroundColor: "rgb(255, 99, 132)",
                              borderColor: "rgb(255, 99, 132)",
                            },

                            {
                              fill: true,
                              label: "Trung lập",
                              data: neutral_count,
                              backgroundColor: "rgb(53, 162, 235)",
                              borderColor: "rgb(75, 192, 192)",
                            },
                            {
                              fill: true,
                              label: "Tích cực",
                              data: positive_count,
                              backgroundColor: "rgb(75, 192, 192)",
                              borderColor: "rgb(75, 192, 192)",
                            },
                          ] : [{
                            fill: true,
                            label: "Tin tức",
                            data: chartQueries[index].data?.result.map((el) => el.data[0].total_count),
                            backgroundColor: "rgb(53, 162, 235)",
                            borderColor: "rgb(75, 192, 192)",
                          },],
                        }}
                      />
                    );
                  }
                }
              })()}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

const Detail = () => {
  const { id } = useParams();

  const [dates, setDates] = useState([moment().add(-30, "day"), moment()]);
  const [modalRangeDate, setModalRangeDate] = useState(false);
  const [charts, setCharts] = useState([]);

  console.log(moment("2020-06-14"));

  const [modalCreateChart, setModalCreateChart] = useState(false);

  const { data, isLoading, isError, refetch } = useQuery(
    ["fetchDetailDashboard", id],
    () => {
      return DashBoardService.detail(id);
    },
    {
      onSuccess: (data) => {
        if (data.code === 0) {
          console.log(data.result.charts);
          const { charts } = data.result;
          setCharts(
            charts.map((el) => ({
              id: el.id,
              name: el.name,
              dashboard_id: el.dashboard_id,
              project_ids: el.project_ids,
              chart_type: {
                id: el.chart_type.id ? el.chart_type.id : el.chart_type,
                name: el.chart_type.name,
              },
              chart_category: {
                id: el.chart_category.id ? el.chart_category.id : el.chart_category,
                name: el.chart_category.name,
              },
            }))
          );
        } else {
          Common.showToast("Vui lòng thử lại", "error");
        }
      },
    }
  );

  if (isLoading) return <p>loading....</p>;
  if (isError) return <p>error</p>;
  return (
    <div className="container-detail">
      <div className="container-detail__header">
        <div className="container-detail__header--left">
          <h2>{data.result.name}</h2>
        </div>
        <div className="container-detail__header--right">
          <Button color="primary" onClick={() => setModalCreateChart(!modalCreateChart)}>
            + Thêm biều đồ
          </Button>
          <div className="container-detail__header-btn--datepicker">
            <Button variant="outline-active" onClick={() => setModalRangeDate(!modalRangeDate)}>
              Chọn ngày
            </Button>
            {modalRangeDate && (
              <Popover position="right" className="container-detail__header--datepicker">
                <DoubleDatePicker
                  value={dates}
                  toggle={() => setModalRangeDate(!modalRangeDate)}
                  callback={(start, end) => {
                    setDates([start, end]);
                  }}
                />
              </Popover>
            )}
          </div>
        </div>
      </div>

      {modalCreateChart && (
        <CreateChart
          dashboard_id={data.result.id}
          modal={modalCreateChart}
          toggle={(d) => {
            if (d) {
              refetch();
            }
            setModalCreateChart(!modalCreateChart);
          }}
        />
      )}
      <Charts charts={charts} dates={dates} />
    </div>
  );
};

export default Detail;
