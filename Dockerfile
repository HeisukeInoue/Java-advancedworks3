# ベースイメージ（軽量なOpenJDK）
FROM eclipse-temurin:17-jdk

# 作業ディレクトリを設定
RUN mkdir -p /app
WORKDIR /app

# 必要なパッケージをインストール（maven）
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# 依存関係を先にダウンロードしてキャッシュを利用
COPY pom.xml /app
RUN mvn dependency:go-offline

# ソースコードはボリュームマウントでマウントされるため、ここではコピーしない

# ホットリロードを有効にする環境変数を設定
ENV JAVA_OPTS="-Dspring.devtools.restart.enabled=true -Dspring.devtools.livereload.enabled=true -Dspring.devtools.restart.poll-interval=2s -Dspring.devtools.restart.quiet-period=1s"

# アプリケーションを実行（ボリュームマウントされたソースコードを使用）
CMD ["sh", "-c", "mvn spring-boot:run $JAVA_OPTS"]
