<script src="{$scriptPath}/PCASClass.js" type="text/javascript"></script>
<table width="100%">
    <tr>
        <td width="80" align="right" valign="top">省/市/区：</td>
        <td><select name="data.province" style="width: 120px"></select>&nbsp;<select name="data.city" style="width: 120px"></select>&nbsp;<select name="data.area" style="width: 120px"></select></td>
    </tr>
    <tr>
        <td align="right" valign="top">邮政编码：</td>
        <td><input type="text" name="data.postcode" /></td>
    </tr>
    <tr>
        <td align="right" valign="top">街道地址：</td>
        <td><textarea name="data.desc" style="width:362px"></textarea></td>
    </tr>
    <tr>
        <td align="right" valign="top">真实姓名：</td>
        <td><input type="text" name="data.name" /></td>
    </tr>
    <tr>
        <td align="right" valign="top">手机号码：</td>
        <td><input type="text" name="data.code" /></td>
    </tr>
</table>
<script language="javascript">
new PCAS("data.province","data.city","data.area");
</script>
