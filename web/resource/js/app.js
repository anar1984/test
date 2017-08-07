function restore() {
    $("#record, #live").removeClass("disabled");
    $("#pause").replaceWith('<a class="button one" id="pause">Pause</a>');
    $(".one").addClass("disabled");

    Fr.voice.stop();
}

function makeWaveform() {
    var analyser = Fr.voice.recorder.analyser;

    var bufferLength = analyser.frequencyBinCount;
    var dataArray = new Uint8Array(bufferLength);

    /**
     * The Waveform canvas
     */
    var WIDTH = 500,
            HEIGHT = 200;

    var canvasCtx = $("#level")[0].getContext("2d");
    canvasCtx.clearRect(0, 0, WIDTH, HEIGHT);

    function draw() {
        var drawVisual = requestAnimationFrame(draw);

        analyser.getByteTimeDomainData(dataArray);

        canvasCtx.fillStyle = 'rgb(200, 200, 200)';
        canvasCtx.fillRect(0, 0, WIDTH, HEIGHT);
        canvasCtx.lineWidth = 2;
        canvasCtx.strokeStyle = 'rgb(0, 0, 0)';

        canvasCtx.beginPath();

        var sliceWidth = WIDTH * 1.0 / bufferLength;
        var x = 0;
        for (var i = 0; i < bufferLength; i++) {
            var v = dataArray[i] / 128.0;
            var y = v * HEIGHT / 2;

            if (i === 0) {
                canvasCtx.moveTo(x, y);
            } else {
                canvasCtx.lineTo(x, y);
            }

            x += sliceWidth;
        }
        canvasCtx.lineTo(WIDTH, HEIGHT / 2);
        canvasCtx.stroke();
    }
    ;
    draw();
}

$(document).ready(function () {
    $(document).on("click", "#record:not(.disabled)", function () {
        Fr.voice.record($("#live").is(":checked"), function () {
            $(".recordButton").addClass("disabled");
            $("#live").addClass("disabled");
            $("#wavtag").hide("");
            $(".one").removeClass("disabled");
            
//            makeWaveform();
//            $("#apd_level").show();
        });
//        Fr.voice.stopRecordingAfter(20000, function () {
//            alert(getMessage("recStppdAftrSmSec"));
//        });
    });
    $(document).on("click", "#recordFor5:not(.disabled)", function () {
        Fr.voice.record($("#live").is(":checked"), function () {
            $(".recordButton").addClass("disabled");
            $("#live").addClass("disabled");
            $(".one").removeClass("disabled");
            makeWaveform();
        });
        Fr.voice.stopRecordingAfter(5000, function () {
            alert("Recording stopped after 5 seconds");
        });
    });
    $(document).on("click", "#pause:not(.disabled)", function () {
        if ($(this).hasClass("resume")) {
            Fr.voice.resume();
            $(this).replaceWith('<a class="button one" id="pause">Pause</a>');
        } else {
            Fr.voice.pause();
            $(this).replaceWith('<a class="button one resume" id="pause">Resume</a>');
        }
    });
    $(document).on("click", "#stop:not(.disabled)", function () {
        restore();
    });
    $(document).on("click", "#play:not(.disabled)", function () {
//        if ($(this).parent().data("type") === "mp3") {
//            Fr.voice.exportMP3(function (url) {
//                $("#audio").attr("src", url);
//                $("#audio")[0].play();
//            }, "URL");
//        } else {
        Fr.voice.export(function (url) {
            $("#audio").attr("src", url);
            $("#audio")[0].play();
        }, "URL");
//        }
        restore();
    });
    $(document).on("click", "#download:not(.disabled)", function () {
        if ($(this).parent().data("type") === "mp3") {
            Fr.voice.exportMP3(function (url) {
                $("<a href='" + url + "' download='MyRecording.mp3'></a>")[0].click();
            }, "URL");
        } else {
            Fr.voice.export(function (url) {
                $("<a href='" + url + "' download='MyRecording.wav'></a>")[0].click();
            }, "URL");
        }
        restore();
    });
    $(document).on("click", "#base64:not(.disabled)", function () {
        if ($(this).parent().data("type") === "mp3") {
            Fr.voice.exportMP3(function (url) {
                console.log("Here is the base64 URL : " + url);
                alert("Check the web console for the URL");
                $("<a href='" + url + "' target='_blank'></a>")[0].click();
            }, "base64");
        } else {
            Fr.voice.export(function (url) {
                console.log("Here is the base64 URL : " + url);
                alert("Check the web console for the URL");
                $("<a href='" + url + "' target='_blank'></a>")[0].click();
            }, "base64");
        }
        restore();
    });


    $(document).on("click", "#save:not(.disabled)", function () {
        function upload(blob) {


            var reader = new window.FileReader();
            reader.readAsDataURL(blob);

            reader.onloadend = function () {
                var st = 'data:audio/wav;base64,';
                base64data = reader.result;
                base64data = base64data.substr(st.length, base64data.length - st.length);
//                console.log('upload-dan gelen:->>>>> '+base64data);
                var file_type = 'audio';
                var fileext = 'wav';
                var d = new Object();
                d.file_base_64 = base64data;
                d.file_extension = fileext;
                d.file_type = file_type;
                conf = JSON.parse('{"kv":{}}');
                conf['kv'] = d;
                var dat = JSON.stringify(conf);
                $.ajax({
                    url: "api/post/upload",
                    type: "POST",
                    data: dat,
                    contentType: "application/json",
                    async: true,
                    success: function (data) {
                        isResultRedirect(JSON.stringify(data));
//                        console.log(JSON.stringify(data));
                        var fname = data.kv.uploaded_file_name;
                        var anls = getVoiceAnalyse(fname);
//                        console.log(JSON.stringify(anls));
                        var st=' <source src="resources/upload/'+fname+'" type="audio/mpeg">';
                        console.log(st);
                        console.log( $("#wavtag").html())
                        $("#wavtag").html(st);
                        console.log( $("#wavtag").html())
                        
                    },
                    error: function () {
                    }
                });

            };
        }



//        Fr.voice.export(function (url) {
//            console.log('download-dan gelen ->>>>>>>>'+url);
////            $("<a href='" + url + "' target='_blank'></a>")[0].click();
//        }, "base64");
//        
        Fr.voice.pause();
        Fr.voice.export(upload, "blob");
        $("#wavtag").show();
        restore();
                
    });
});

