$(document).ready(function () {
    $("input[id=currentDate]").val(today());
    $("input[id=currentDate2]").val(today());

    let date1, date2, currentDate

    if ($("input[id=currentDate]").val() !== null && $("input[id=currentDate2]").val() !== null) {
        date1 = formatDate($("input[id=currentDate]").val());
        date2 = formatDate($("input[id=currentDate2]").val());
        currentDate = formatDate(new Date());
    }

    $("input[name=totalRepayment]").val(0.00);

    if (date1 > currentDate || date1 === currentDate) {
        if (date1 < date2 || date1 === date2) {
            $("form[id=recurringPaymentForm]").find(':input[type=submit]').prop('disabled', false);
        } else {
            $("form[id=recurringPaymentForm]").find(':input[type=submit]').prop('disabled', true);
        }
    } else {
        $("form[id=recurringPaymentForm]").find(':input[type=submit]').prop('disabled', true);
    }

    $("#recurringPaymentForm").bind("keyup change", function () {
        //var date1 = $("input[name=currentDate]").val();
        //var date2 = $("input[id=currentDate2]").val();
        //console.log(date2);

        let date1 = formatDate($("input[id=currentDate]").val());
        let date2 = formatDate($("input[id=currentDate2]").val());
        let currentDate = formatDate(new Date());

        if (date1 > currentDate || date1 === currentDate) {
            if (date1 < date2 || date1 === date2) {
                $("form[id=recurringPaymentForm]").find(':input[type=submit]').prop('disabled', false);
            } else {
                $("form[id=recurringPaymentForm]").find(':input[type=submit]').prop('disabled', true);
            }
        } else {
            $("form[id=recurringPaymentForm]").find(':input[type=submit]').prop('disabled', true);
        }

    });


    $("#creditApplication").bind("keyup change", function () {
        let creditAmount = $("input[name=creditAmount]").val();
        let months = $("select[name=numberOfMonths]").val();
        let myDataArray = $("select[name=creditType]").find(':selected').data();

        //alert(myDataArray["country"]);
        //alert(myDataArray["city"]);
        //let creditRates = $("select[name=creditType]").find(':selected').data('rates');
        //let commission = $("select[name=creditType]").find(':selected').data('commission');
        let creditRates = myDataArray["rates"];
        let commission = myDataArray["commission"];

        if(creditAmount !== ""){
            $("input[name=totalRepayment]").val((f(creditAmount, months, creditRates, commission).valueOf() * months).toFixed(2));
            $("input[name=monthRepayment]").val((f(creditAmount, months, creditRates, commission).valueOf()).toFixed(2));
        } else {
            $("input[name=totalRepayment]").val(0.00);
            $("input[name=monthRepayment]").val(0.00);
        }



    });


    if ($("select[name=currencyFrom]").find(':selected').text()
        .localeCompare($("select[name=currencyTo]").find(':selected').text()) == 0) {
        $("select[name=currencyFrom]").find(':input[type=submit]').prop('disabled', true);
        $("#currencyExchangeMessage").text("Wybierz inną walutę!");
        $("input[name=totalPayment]").val(0.00);
    }

    $("#currencyForm").bind("keyup change", function () {
        //var currencyNameFrom = $("select[name=currencyFrom]").find(':selected').text(); // pobieram name
        //var currencyNameTo = $("select[name=currencyTo]").find(':selected').text() // pobieram name

        if ($("select[name=currencyFrom]").find(':selected').text()
            .localeCompare($("select[name=currencyTo]").find(':selected').text()) == 0) {
            $(this).find(':input[type=submit]').prop('disabled', true);
            $("#currencyExchangeMessage").text("Wybierz inną walutę!");
            $("input[name=totalPayment]").val(0.00);
        } else {
            $(this).find(':input[type=submit]').prop('disabled', false);
            $("#currencyExchangeMessage").text("")

            $("input[name=totalPayment]").val((f2(
                $("input[name=value]").val(),
                $("select[name=currencyFrom]").find(':selected').val(),
                $("select[name=currencyTo]").find(':selected').val()
            ).valueOf()).toFixed(2));
        }
    });

    $("form.search#search-users").submit(function () {
        let pageableSize = document.querySelector("#main > div > div > nav > ul > li.page-item.active > a > span").firstChild.textContent;

        document.querySelector("#search-users > div:nth-child(1) > input[type=hidden]:nth-child(2)").value = pageableSize;
        document.querySelector("#search-users > div:nth-child(1) > input[type=hidden]:nth-child(1)").value = 0;
    });

    $("#investment").bind("keyup change", function () {
        let investmentAmount = $("input[name=investmentAmount]").val();
        let months = $("select[name=numberOfMonths]").val();
        let investmentsRates = $("select[name=investmentType]").find(':selected').data('rates');


        $("input[name=totalRepayment]").val((f3(investmentAmount, months, investmentsRates).valueOf()).toFixed(2));
    });

    let _MS_PER_DAY = 1000 * 60 * 60 * 24;

    function dateDiffInDays(a, b) {
        // Discard the time and time-zone information.
        let utc1 = Date.UTC(a.getFullYear(), a.getMonth(), a.getDate());
        let utc2 = Date.UTC(b.getFullYear(), b.getMonth(), b.getDate());

        return Math.floor((utc2 - utc1) / _MS_PER_DAY);
    }

    function f(creditAmount, months, creditRates, commission) {
        let sum = 0.0;
        let numberOfInstallmentsPaidDuringTheYear = 12;
        creditAmount = parseFloat(creditAmount) + (creditAmount * commission);

        for (let i = 1; i <= months; i++) {
            sum = sum + Math.pow((1 + ((0.01 * creditRates) / numberOfInstallmentsPaidDuringTheYear)), 0 - i);
        }
        return creditAmount / sum;
    }

    function f2(value, currencyFrom, currencyTo) {
        return (currencyFrom * value) / currencyTo;
    }

    function f3(investmentAmount, months, investmentsRates) {
        let profit = (investmentAmount *  dateDiffInDays(new Date(), new Date(new Date().setMonth(new Date().getMonth() + months))) * (investmentsRates * 0.01)) / 365.0;
        let profitMinusTax = profit - (profit * 0.19);
        if(investmentAmount !== ""){
            return parseFloat(investmentAmount) + profitMinusTax;
        } else {
            return 0;
        }
    }

    function today() {
        let now = new Date();
        let day = ("0" + now.getDate()).slice(-2);
        let month = ("0" + (now.getMonth() + 1)).slice(-2);
        let today = now.getFullYear() + "-" + (month) + "-" + (day);

        return today;
    }


    function formatDate(date) {
        var d = new Date(date),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();

        if (month.length < 2)
            month = '0' + month;
        if (day.length < 2)
            day = '0' + day;

        return [year, month, day].join('-');
    }
});