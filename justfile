build:
    @echo "Building image for amd64 (version: 0.1.0)..."
    docker buildx build --platform linux/amd64 -t caso-de-estudio:0.1.0-amd64 --load .
    @echo "Done."
