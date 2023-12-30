$('document').ready(function () {
    $("#search-form").submit(function (event) {
        event.preventDefault();
        searchSubmit();
    });
});

function searchSubmit() {
    var fromCurrency = $("#fromCurrency").val();
    var toCurrency = $('#toCurrency').val();
    $("#bth-search").prop("disabled", true);

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/search?fromCurrency=" + fromCurrency + "&toCurrency=" + toCurrency,
        dataType: 'json'
    }).done(function (dataset) {
        displayRealtimePanel(dataset);
        changeAmount();
        createGraph(dataset);

        $("#bth-search").prop("disabled", false);
    }).fail(function (error) {
        displayRealtimePanelError(error);
        $("#btn-search").prop("disabled", false);
    });
}

function displayRealtimePanel(dataset) {
    var fromCurrency = $("#fromCurrency").val();
    var toCurrency = $('#toCurrency').val();
    var exchangeRate = dataset.realtimeRate.exchangeRate;
    var fixedRate = exchangeRate.toFixed(4);
    var card = $("<div>").addClass("card");
    var cardHeader = $("<div>").addClass("card-header").text("Real-time exchange rate");
    var cardBody = $("<div>").addClass("card-body form-inline");
    var h5 = $("<h5>").addClass("card-title my-1").attr("id", "exchange-rate").text(fixedRate).css("font-weight", "bold");
    var label = $("<label>").addClass("my-1 mr-2").text(fromCurrency + " amount").attr("for", "input-number").css("margin-left", "20px");
    var input = $("<input>").addClass("form-control my-1").attr("min", "1").attr("type", "number").attr("value", "1").attr("id", "input-number").css("width", "100px");
    var labelExchanged = $("<label>").addClass("my-1 mr-2").text(toCurrency + " amount " + fixedRate).attr("id", "exchanged-amount").css("margin-left", "20px");
    var cardFooter = $("<div>").addClass("card-footer text-muted").text(fromCurrency + " --> " + toCurrency);
    cardBody.append(h5, label, input, labelExchanged);
    card.append(cardHeader, cardBody, cardFooter); // Додайте аналіз до картки

    $('#realtime-panel').html(card); // Оновіть панель у реальному часі новою карткою
    $("#search-form-footer").text("Current data as per day: " + dataset.realtimeRate.date);
    $("#analysis").html(analysisDiv);
}


// Переконайтесь, що ви викликаєте displayAnalysis десь в коді, наприклад, після отримання даних з сервера:
// displayAnalysis(dataset.analysis);

function displayRealtimePanelError(error) {

    var card = $("<div>").addClass("card");
    var cardHeader = $("<div>").addClass("card-header").text("Error");
    var cardBody = $("<div>").addClass("card-body form-inline");
    var h6 = $("<h6>").addClass("card-title my-1").text(error.responseText).css("color", "red");
    var cardFooter = $("<div>").addClass("card-footer text-muted").text("Please wait 1 minute or chose different currencies pair").css("font-weight", "bold");

    //Constructing card element
    cardBody.append(h6);
    card.append(cardHeader, cardBody, cardFooter);

    $('#realtime-panel').html(card);
}

function changeAmount() {
    var exchangeRate = $("#exchange-rate").text();
    var toCurrency = $('#toCurrency').val();
    var input = $("#input-number");
    var labelExchanged = $("#exchanged-amount");

    input.change('input', function () {
        var multiply = exchangeRate * input.val();
        var fixedMultiply = multiply.toFixed(4);
        labelExchanged.text(toCurrency + " amount " + fixedMultiply);
    });

}

function createGraph(dataset) {
    var fromCurrency = $("#fromCurrency").val();
    var toCurrency = $('#toCurrency').val();
    var historicalRates = dataset.historicalRates;


    //refactoring the data into structure needed for graph creation
    var tabOfTabs = [];
    var singleValue;
    for (var i = 0; i < historicalRates.length; i++) {
        var tempTab = [];
        for (var j = 0; j < historicalRates[i].length; j++) {
            var date = historicalRates[i][j].date;
            var rate = historicalRates[i][j].exchangeRate;
            tempTab.push(singleValue = {
                t: date,
                y: rate
            });
        }
        tabOfTabs.push(tempTab);
    }

    //removing canvas with old graph
    var chart = $("#chart");
    if (typeof chart !== "undefined") {
        chart.remove();
        var canvas = $("<canvas>").attr("id", "chart");
        $('#graph-panel').html(canvas);
    }

    //creating new graph
    var data = {
        datasets: [{
            label: fromCurrency + " to " + toCurrency,
            backgroundColor: 'rgb(255, 99, 132)',
            borderColor: 'rgb(255, 99, 132)',
            data: tabOfTabs[0],
            pointRadius: 0,
            fill: false,
            lineTension: 0,
            borderWidth: 2
        }]
    };
    var options = {
        title: {
            display: true,
            text: 'Historical exchange chart'
        },
        scales: {
            xAxes: [{
                type: 'time',
                unit: 'day',
                unitStepSize: 1,
                time: {
                    displayFormats: {
                        'millisecond': 'DD MMM YY',
                        'second': 'DD MMM YY',
                        'minute': 'DD MMM YY',
                        'hour': 'DD MMM YY',
                        'day': 'DD MMM YY',
                        'week': 'DD MMM YY',
                        'month': 'DD MMM YY',
                        'quarter': 'DD MMM YY',
                        'year': 'DD MMM YY'
                    }
                },
                distribution: 'series',
                ticks: {
                    source: 'data',
                    autoSkip: true
                },
                scaleLabel: {
                    display: true,
                    labelString: 'Time Period'
                }
            }],
            yAxes: [{
                scaleLabel: {
                    display: true,
                    labelString: 'Exchange Rate'
                }
            }]
        }
    };
    var ctx = $("#chart");
    var myLineChart = new Chart(ctx, {
        type: 'line',
        data: data,
        options: options
    });

    createButtons(tabOfTabs);
    createButtonsEvents(data, tabOfTabs, myLineChart);
}

function createButtons(tabOfTabs) {
    var buttonGroup = $("<div>").attr("id", "button-group");

    for (var i = 0; i < tabOfTabs.length; i++) {
        switch (i) {
            case 0:
                var button1 = $("<button>").addClass("btn btn-primary my-1 update-buttons").attr("id", "1W").text("1W").css("margin-left", "15px");
                buttonGroup.append(button1);
                break;
            case 1:
                var button2 = $("<button>").addClass("btn btn-primary my-1 update-buttons").attr("id", "2W").text("2W").css("margin-left", "15px");
                buttonGroup.append(button2);
                break;
            case 2:
                var button3 = $("<button>").addClass("btn btn-primary my-1 update-buttons").attr("id", "1M").text("1M").css("margin-left", "15px");
                buttonGroup.append(button3);
                break;
            case 3:
                var button4 = $("<button>").addClass("btn btn-primary my-1 update-buttons").attr("id", "2M").text("2M").css("margin-left", "15px");
                buttonGroup.append(button4);
                break;
            case 4:
                var button5 = $("<button>").addClass("btn btn-primary my-1 update-buttons").attr("id", "6M").text("6M").css("margin-left", "15px");
                buttonGroup.append(button5);
                break;
            case 5:
                var button6 = $("<button>").addClass("btn btn-primary my-1 update-buttons").attr("id", "1Y").text("1Y").css("margin-left", "15px");
                buttonGroup.append(button6);
                break;
            case 6:
                var button7 = $("<button>").addClass("btn btn-primary my-1 update-buttons").attr("id", "2Y").text("2Y").css("margin-left", "15px");
                buttonGroup.append(button7);
                break;
            case 7:
                var button8 = $("<button>").addClass("btn btn-primary my-1 update-buttons").attr("id", "5Y").text("5Y").css("margin-left", "15px");
                buttonGroup.append(button8);
                break;
            case 8:
                var button9 = $("<button>").addClass("btn btn-primary my-1 update-buttons").attr("id", "10Y").text("10Y").css("margin-left", "15px");
                buttonGroup.append(button9);
                break;
        }
    }

    $('#periods').html(buttonGroup);
}

function createButtonsEvents(data, tabOfTabs, myLineChart) {
    $("#1W").click(function () {
        data.datasets[0].data = tabOfTabs[0];
        myLineChart.update();
    });
    $("#2W").click(function () {
        data.datasets[0].data = tabOfTabs[1];
        myLineChart.update();
    });
    $("#1M").click(function () {
        data.datasets[0].data = tabOfTabs[2];
        myLineChart.update();
    });
    $("#2M").click(function () {
        data.datasets[0].data = tabOfTabs[3];
        myLineChart.update();
    });
    $("#6M").click(function () {
        data.datasets[0].data = tabOfTabs[4];
        myLineChart.update();
    });
    $("#1Y").click(function () {
        data.datasets[0].data = tabOfTabs[5];
        myLineChart.update();
    });
    $("#2Y").click(function () {
        data.datasets[0].data = tabOfTabs[6];
        myLineChart.update();
    });
    $("#5Y").click(function () {
        data.datasets[0].data = tabOfTabs[7];
        myLineChart.update();
    });
    $("#10Y").click(function () {
        data.datasets[0].data = tabOfTabs[8];
        myLineChart.update();
    });
}