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

var i = 0;

function setupSelect(element, data) {
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
    if (i > 0) updateWeather();
    else i++;
}

$(document).ready(function () {
    $.ajax({url: "/cities"}).then(function (data) {
        setupSelect($("#city"), data)
    });
    $.ajax({url: "/weatherServices"}).then(function (data) {
        setupSelect($("#weatherService"), data)
    });
});