git pull

sbt11 clean compile

rm -rf ../gh-pages/main/*
rm -rf ../gh-pages/test/*

cp -r target/scala-2.9.1/classes.sxr/* ../gh-pages/main/

cp -r target/scala-2.9.1/test-classes.sxr/* ../gh-pages/test/

cd ../gh-pages/

git pull

git add \*.html

git commit -a

