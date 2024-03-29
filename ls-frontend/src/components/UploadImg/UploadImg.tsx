import React, { useState, useEffect } from "react";
import { useMutation } from "react-query";
import "./uploadimg.scss";
import UploadService from "services/UploadService";

export interface IUploadImgModel {
  className?: string;
  callback?: (url) => void;
  value?: any;
}

export const UploadImg = (props: IUploadImgModel) => {
  const { className, callback, value } = props;
  const [selectedFile, setSelectedFile] = useState<any>();
  const [src, setSrc] = useState(value ? value : "");
  const mutation = useMutation(
    (file) => {
      return UploadService.uploadImage(file);
    },
    {
      onSuccess: (data) => {
        setSrc(data.fileDownloadUri);
        callback(data.fileDownloadUri);
      },
    }
  );

  return (
    <div className={`base-uploadimg ${className ? className : ""}`}>
      <div className="uploadimg--inner">
        <label htmlFor="uploadImg">
          {selectedFile ? (
            <img src={src} />
          ) : (
            <svg width="61" height="61" viewBox="0 0 61 61" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path
                d="M34.7913 34.7916V60.5416H26.208V34.7916H0.458008V26.2083H26.208V0.458252H34.7913V26.2083H60.5413V34.7916H34.7913Z"
                fill="#C4C4C4"
              />
            </svg>
          )}
        </label>
        <input
          id="uploadImg"
          type="file"
          onChange={(e) => {
            if (e.target.files && e.target.files.length > 0) {
              setSelectedFile(e.target.files[0]);
              const formData: any = new FormData();
              formData.append("file", e.target.files[0]);
              mutation.mutate(formData);
            }
          }}
        />
      </div>
    </div>
  );
};
