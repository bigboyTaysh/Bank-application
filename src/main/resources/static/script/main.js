$(document).ready(function () {
    $("#creditApplication").bind("keyup change", function () {
        var creditAmount = $("input[name=creditAmount]").val();
        var months = $("select[name=numberOfMonths]").val();
        var creditRates = $("select[name=creditType]").find(':selected').data('rates');
        console.log(creditRates);

        $("input[name=totalRepayment]").val((f(creditAmount, months, creditRates).valueOf() * months).toFixed(2));
        $("input[name=monthRepayment]").val((f(creditAmount, months, creditRates).valueOf()).toFixed(2));
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

    $("form.search#search-users").submit(function () {
        var pageableSize = document.querySelector("#main > div > div > nav > ul > li.page-item.active > a > span").firstChild.textContent;
        var page = document.querySelector("#main > div > div > nav > ul.pagination.pagination-sm > li.page-item.active > a").textContent;


        document.querySelector("#search-users > div:nth-child(1) > input[type=hidden]:nth-child(2)").value = pageableSize;
        document.querySelector("#search-users > div:nth-child(1) > input[type=hidden]:nth-child(1)").value = 0;


    });
});