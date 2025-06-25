# Use an official Python runtime as a parent image
FROM docker.io/python:3-slim

# Set the working directory to /app
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY requirements.txt /app
COPY launcher.py /app
COPY k8s/job-image-gen-store.yaml /app/k8s/

# Install any needed packages specified in requirements.txt
RUN pip install -r requirements.txt

# Install opentelemetry-bootstrap
RUN opentelemetry-bootstrap --action=install

# Uninstall opentelemetry-instrumentation-aws-lambda
# This is a temporary fix for a bug in the opentelemetry-instrumentation-aws-lambda package.
RUN pip uninstall opentelemetry-instrumentation-aws-lambda -y

# Make port 5001 available to the world outside this container
EXPOSE 5001

# Define environment variables
ENV OTEL_LOGS_EXPORTER="otlp"
ENV OTEL_METRICS_EXPORTER="otlp"
ENV OTEL_TRACES_EXPORTER="otlp"
ENV OTEL_EXPORTER_OTLP_ENDPOINT="localhost:4317"
ENV OTEL_PYTHON_DISABLED_INSTRUMENTATIONS=aws_lambda
ENV OTEL_SERVICE_NAME=launcher

# Run app.py when the container launches
CMD ["opentelemetry-instrument","python","launcher.py"]