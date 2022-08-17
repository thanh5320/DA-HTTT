import React from "react";
import BeatLoader from "react-spinners/BeatLoader";
import './loading.scss';

const Loading = ({size}) => {
  return (
    <div className="container-loading">
      <BeatLoader color={"#1850A8"} loading={true} size={size} />
    </div>
  );
};

export default Loading;
