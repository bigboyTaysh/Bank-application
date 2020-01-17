$(document).ready(function () {
    $("#creditApplication").bind("keyup change", function () {
        var creditAmount = $("input[name=creditAmount]").val();
        var months = $("select[name=numberOfMonths]").val();
        var creditRates = $("select[name=creditRates]").val();

        $("input[name=totalRepayment]").val((f(creditAmount, months, creditRates).valueOf() * months).toFixed(2));
        $("input[name=monthRepayment]").val((f(creditAmount, months, creditRates).valueOf()).toFixed(2));
    });

    function f(creditAmount, months, creditRates) {
        let sum = 0.0;
        creditAmount = creditAmount * 1.0599;
        console.log(creditAmount + " " + months + " " + creditRates)

            for (let i = 1; i <= months; i++){
                sum = sum + Math.pow((1 + ((0.01 * creditRates)/12)),0-i);
            }
        return creditAmount / sum;
    }
});