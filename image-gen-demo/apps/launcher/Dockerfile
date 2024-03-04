FROM nixery.dev/shell/kubectl

WORKDIR /app
COPY launcher.sh /app/

RUN chmod +x launcher.sh

ENTRYPOINT ["/app/launcher.sh"]
