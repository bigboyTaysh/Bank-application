$(document).ready(function () {
    var now = new Date();
    var day = ("0" + now.getDate()).slice(-2);
    var month = ("0" + (now.getMonth() + 1)).slice(-2);
    var today = now.getFullYear()+"-"+(month)+"-"+(day) ;
    $("input[id=currentDate]").val(today);
    $("input[id=currentDate2]").val(today);

    if($("input[id=currentDate]").val() !== null && $("input[id=currentDate2]").val() !== null){
        var date1 = formatDate($("input[id=currentDate]").val());
        var date2 = formatDate($("input[id=currentDate2]").val());
        var currentDate = formatDate(new Date());
    }


    $("input[name=totalRepayment]").val((f3(
        $("input[name=investmentAmount]").val(),
        $("select[name=numberOfMonths]").val(),
        $("select[name=investmentType]").find(':selected').data('rates')
    ).valueOf()).toFixed(2));

    if(date1 > currentDate || date1 === currentDate){
        if(date1 < date2 || date1 === date2){
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

        var date1 = formatDate($("input[id=currentDate]").val());
        var date2 = formatDate($("input[id=currentDate2]").val());
        var currentDate = formatDate(new Date());

        if(date1 > currentDate || date1 === currentDate){
            if(date1 < date2 || date1 === date2){
                $("form[id=recurringPaymentForm]").find(':input[type=submit]').prop('disabled', false);
            } else {
                $("form[id=recurringPaymentForm]").find(':input[type=submit]').prop('disabled', true);
            }
        } else {
            $("form[id=recurringPaymentForm]").find(':input[type=submit]').prop('disabled', true);
        }

    });


    $("#creditApplication").bind("keyup change", function () {
        var creditAmount = $("input[name=creditAmount]").val();
        var months = $("select[name=numberOfMonths]").val();
        var creditRates = $("select[name=creditType]").find(':selected').data('rates');

        $("input[name=totalRepayment]").val((f(creditAmount, months, creditRates).valueOf() * months).toFixed(2));
        $("input[name=monthRepayment]").val((f(creditAmount, months, creditRates).valueOf()).toFixed(2));
    });


    if($("select[name=currencyFrom]").find(':selected').text()
        .localeCompare($("select[name=currencyTo]").find(':selected').text()) == 0){
        $("select[name=currencyFrom]").find(':input[type=submit]').prop('disabled', true);
        $("#currencyExchangeMessage").text("Wybierz inną walutę!");
        $("input[name=totalPayment]").val(0.00);
    }

    $("#currencyForm").bind("keyup change", function () {
        //var currencyNameFrom = $("select[name=currencyFrom]").find(':selected').text(); // pobieram name
        //var currencyNameTo = $("select[name=currencyTo]").find(':selected').text() // pobieram name

        if($("select[name=currencyFrom]").find(':selected').text()
            .localeCompare($("select[name=currencyTo]").find(':selected').text()) == 0){
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
        var pageableSize = document.querySelector("#main > div > div > nav > ul > li.page-item.active > a > span").firstChild.textContent;
        var page = document.querySelector("#main > div > div > nav > ul.pagination.pagination-sm > li.page-item.active > a").textContent;


        document.querySelector("#search-users > div:nth-child(1) > input[type=hidden]:nth-child(2)").value = pageableSize;
        document.querySelector("#search-users > div:nth-child(1) > input[type=hidden]:nth-child(1)").value = 0;
    });

    $("#investment").bind("keyup change", function () {
        var investmentAmount = $("input[name=investmentAmount]").val();
        var months = $("select[name=numberOfMonths]").val();
        var investmentsRates = $("select[name=investmentType]").find(':selected').data('rates');


        $("input[name=totalRepayment]").val((f3(investmentAmount, months, investmentsRates).valueOf()).toFixed(2));
    });

    function f(creditAmount, months, creditRates) {
        let sum = 0.0;
        let commission = 1.0;
        let numberOfInstallmentsPaidDuringTheYear = 12;
        creditAmount = creditAmount * commission;

            for (let i = 1; i <= months; i++){
                sum = sum + Math.pow((1 + ((0.01 * creditRates)/numberOfInstallmentsPaidDuringTheYear)),0-i);
            }
        return creditAmount / sum;
    }

    function f2(value, currencyFrom, currencyTo) {
        return (currencyFrom*value)/currencyTo;
    }

    function f3(investmentAmount, months, investmentsRates) {
        let newDate = new Date();
        let dateWithCountOfMonths = new Date(newDate.setMonth(newDate.getMonth()+months));

        let differenceInTime = dateWithCountOfMonths.getTime() - newDate.getTime();
        let differenceInDays = differenceInTime / (1000 * 3600 * 24)

        let sum = 0.0;
        let commission = 1.0;
        let numberOfDays = differenceInDays;
        let year = 360;
        investmentAmount = investmentAmount * commission;

        sum = (investmentAmount*numberOfDays*investmentsRates)/year;
        sum = sum + investmentAmount;
        return sum;
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