FROM python:3.9.5-slim
RUN apt-get update \
    && apt-get -y install libpq-dev gcc
COPY . .
RUN pip3 install -r requirements.txt
CMD python3 main.py