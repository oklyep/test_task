$(document).ready(function () {
    function updateWeather() {
        $.ajax({
            url: "/weather?city=" + encodeURI(localStorage.city) + '&weatherService=' + encodeURI(localStorage.weatherService),
            success: function (data) {
                $("#temp").html(data["temp"]);
                $("#wind").html(data["wind"]);
                $("#humidity").html(data["humidity"]);
                $("#error").hide();
            },
            error: function () {
                $("#error").show();
            }
        })
    }

    var loaded = 0;

    $.each([["/cities", $("#city")], ["/weatherServices", $("#weatherService")]],
        function (index, value) {
            $.ajax({
                url: value[0],
                success: function (data) {
                    var element = value[1];
                    Object.keys(data).forEach(function (value) {
                        element.append("<option value='" + value + "'>" + data[value] + "</option>")
                    });
                    element.bind("change", function () {
                        localStorage[element.attr("id")] = this.value;
                        updateWeather();
                    });
                    if (element.find("option[value='" + localStorage[element.attr("id")] + "']").length > 0)
                        element.val(localStorage[element.attr("id")]);
                    else {
                        var val = element.find("option")[0].value;
                        element.val(val);
                        localStorage[element.attr("id")] = val;
                    }
                    loaded++;
                }
            }).then(function setupSelect() {
                    if (loaded >= 2)
                        updateWeather();
                }
            )
        });
});