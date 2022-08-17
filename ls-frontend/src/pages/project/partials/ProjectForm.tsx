import Button from "components/button/button";
import Input from "components/input/input";
import TagsInput from "components/tagsInput/tagsInput";
import React from "react";
import { Controller, useFieldArray, useFormContext } from "react-hook-form";

const ProjectForm = () => {
  const { register, control } = useFormContext();
  const { fields, append, remove } = useFieldArray({
    control,
    name: "rules",
  });
  return (
    <div className="container-project-form">
      <Input label="Tên bộ truy vấn" name="name" register={register("name")} />

      <div className="container-project-form__header">
        <p>Từ khóa</p>
        <Button variant="outline" onClick={() => append({ main_keywords: [], sub_keywords: [], exclude_keywords: [] })}>
          + Thêm từ khóa
        </Button>
      </div>
      <div>
        {fields.map((field, index) => (
          <div key={field.id} className="container-project-form__key">
            <p className="container-project-form__key-title">
              Bộ từ khóa {index + 1} <span onClick={() => remove(index)}>Xóa từ khóa {index + 1}</span>
            </p>
            <div>
              <Controller
                control={control}
                name={`rules.${index}.main_keywords`}
                render={({ field: { onChange, value } }) => (
                  <TagsInput
                    tagsData={value}
                    addTag={(tags) => onChange(tags)}
                    removeTag={(tags) => onChange(tags)}
                    acceptPaste={false}
                    maxLength={50}
                    label="Từ khoá chính"
                    required={true}
                    onRemoveAllTag={() => onChange([])}
                  />
                )}
              />
              <Controller
                control={control}
                name={`rules.${index}.sub_keywords`}
                render={({ field: { onChange, value } }) => (
                  <TagsInput
                    tagsData={value}
                    addTag={(tags) => onChange(tags)}
                    removeTag={(tags) => onChange(tags)}
                    acceptPaste={false}
                    maxLength={50}
                    label="Từ khoá phụ"
                    onRemoveAllTag={() => onChange([])}
                  />
                )}
              />
              <Controller
                control={control}
                name={`rules.${index}.exclude_keywords`}
                render={({ field: { onChange, value } }) => (
                  <TagsInput
                    tagsData={value}
                    addTag={(tags) => onChange(tags)}
                    removeTag={(tags) => onChange(tags)}
                    acceptPaste={false}
                    maxLength={50}
                    label="Từ khoá ngoại lệ"
                    onRemoveAllTag={() => onChange([])}
                  />
                )}
              />
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ProjectForm;
