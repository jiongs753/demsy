<style>
.bankstable {
	
}

.bankstable td {
	padding: 15px;
}

.bankstable label {
	background-color: #ffffff;
	padding: 10px;
	border: 1px #9C9C9C solid;
	cursor: pointer;
}

.bankstable .title {
	border-bottom: 1px dotted #9C9C9C;
	margin-bottom: 10px;
	text-align: left;
	padding: 10px;
	font-size: 14px;
	font-weight: bold;
}
.bankstable .ui-btns{
	border-top: 1px dotted #9C9C9C;
	padding: 10px;
	margin-top: 5px;
}
.bankstable div.ui-btn{
	border: 1px solid #d74c00;
	background: #ffffff;
	padding: 1px;
	width: 90px;
	position: absolute;
	right: 45px;
}
.bankstable input.ui-btn{
	border: 0;
	background: #ff9400;
	padding: 5px 0 5px 0;
	color: #ffffff;
	font-size: 14px;
	font-weight: bold;
	width: 100%;
	height: 100%;
	cursor: pointer;
}
</style>
<form id="bankform_{$block.id}">
	<table width="100%" class="bankstable">
		<caption class="title">请选择您的付款方式：</caption>
		<tr>
			<td><input type="radio" name="pay_bank" id="directPay" value="directPay" checked /><label for="directPay"><img src="{$contextPath}/themes2/images/banks/alipay_1.gif" border="0" /> </label></td>
		</tr>
		<tr>
			<td><input type="radio" name="pay_bank" id="ICBCB2C" value="ICBCB2C" /><label for="ICBCB2C"><img src="{$contextPath}/themes2/images/banks/ICBC_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="CMB" value="CMB" /><label for="CMB"><img src="{$contextPath}/themes2/images/banks/CMB_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="CCB" value="CCB" /><label for="CCB"><img src="{$contextPath}/themes2/images/banks/CCB_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="BOCB2C" value="BOCB2C" /><label for="BOCB2C"><img src="{$contextPath}/themes2/images/banks/BOC_OUT.gif" border="0" /> </label></td>
		</tr>
		<tr>
			<td><input type="radio" name="pay_bank" id="ABC" value="ABC" /><label for="ABC"><img src="{$contextPath}/themes2/images/banks/ABC_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="COMM" value="COMM" /><label for="COMM"><img src="{$contextPath}/themes2/images/banks/COMM_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="SPDB" value="SPDB" /><label for="SPDB"><img src="{$contextPath}/themes2/images/banks/SPDB_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="GDB" value="GDB" /><label for="GDB"><img src="{$contextPath}/themes2/images/banks/GDB_OUT.gif" border="0" /> </label></td>
		</tr>
		<tr>
			<td><input type="radio" name="pay_bank" id="CITIC" value="CITIC" /><label for="CITIC"><img src="{$contextPath}/themes2/images/banks/CITIC_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="CEBBANK" value="CEBBANK" /><label for="CEBBANK"><img src="{$contextPath}/themes2/images/banks/CEB_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="CIB" value="CIB" /><label for="CIB"><img src="{$contextPath}/themes2/images/banks/CIB_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="SDB" value="SDB" /><label for="SDB"><img src="{$contextPath}/themes2/images/banks/SDB_OUT.gif" border="0" /> </label></td>
		</tr>
		<tr>
			<td><input type="radio" name="pay_bank" id="CMBC" value="CMBC" /><label for="CMBC"><img src="{$contextPath}/themes2/images/banks/CMBC_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="HZCBB2C" value="HZCBB2C" /><label for="HZCBB2C"><img src="{$contextPath}/themes2/images/banks/HZCB_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="SHBANK" value="SHBANK" /><label for="SHBANK"><img src="{$contextPath}/themes2/images/banks/SHBANK_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="NBBANK" value="NBBANK" /><label for="NBBANK"><img src="{$contextPath}/themes2/images/banks/NBBANK_OUT.gif" border="0" /> </label></td>
		</tr>
		<tr>
			<td><input type="radio" name="pay_bank" id="SPABANK" value="SPABANK" /><label for="SPABANK"><img src="{$contextPath}/themes2/images/banks/SPABANK_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="BJRCB" value="BJRCB" /><label for="BJRCB"><img src="{$contextPath}/themes2/images/banks/BJRCB_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="ICBCBTB" value="ICBCBTB" /><label for="ICBCBTB"><img src="{$contextPath}/themes2/images/banks/ENV_ICBC_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="CCBBTB" value="CCBBTB" /><label for="CCBBTB"><img src="{$contextPath}/themes2/images/banks/ENV_CCB_OUT.gif" border="0" /> </label></td>
		</tr>
		<tr>
			<td><input type="radio" name="pay_bank" id="SPDBB2B" value="SPDBB2B" /><label for="SPDBB2B"><img src="{$contextPath}/themes2/images/banks/ENV_SPDB_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="ABCBTB" value="ABCBTB" /><label for="ABCBTB"><img src="{$contextPath}/themes2/images/banks/ENV_ABC_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="fdb101" value="fdb101" /><label for="fdb101"><img src="{$contextPath}/themes2/images/banks/FDB_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="PSBC-DEBIT" value="PSBC-DEBIT" /><label for="PSBC-DEBIT"><img src="{$contextPath}/themes2/images/banks/PSBC_OUT.gif" border="0" /> </label></td>
		</tr>
		<tfoot>
			<tr>
				<td colspan=4><div  class="ui-btns"><div class="ui-btn"><input type="submit" class="btnNextStep ui-btn" value="下一步" /></div></div></td>
			</tr>
		</tfoot>
	</table>
</form>
<script>
var $bankform=$("#bankform_{$block.id}");
$("input",$bankform).click(function(){
	$("label",$bankform).each(function(){
		$(this).css("border","1px #9C9C9C solid");
	});
	$("label",$(this).parent()).css("border","2px solid #fd0200");
});
</script>
