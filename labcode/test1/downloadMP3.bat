..\..\tools\rtmpdump-2.4\rtmpdump -r "rtmpe://fms1.uniqueradio.jp/" -y "aandg11" -a "?rtmp://fms-base1.mitene.ad.jp/agqr/" -A 0 -B 10 -v -o output.flv
..\..\tools\ffmpeg\bin\ffmpeg -i output.flv output.mp3