<div class="d">
    {if $user == null}
    <table width="100%" border="0" align="center" cellpadding="1" cellspacing="1">
        <tr>
            <td align="center"><span style="color: red">您尚未登录或登录已过期!</span></td>
        </tr>
    </table>
    {else}
    <form onsubmit="return false;">
        <table width="100%" border="0" align="center" cellpadding="1" cellspacing="1">
            <tr>
                <td class="label">登录帐号：</td>
                <td class="input">{$user.username}</td>
            </tr>
            <tr>
                <td class="label">原始密码：</td>
                <td class="input"><input type='password' name="_loginpwd_" />
                </td>
            </tr>
            <tr>
                <td class="label">新密码：</td>
                <td class="input"><input type='password' name="rawPassword" />
                </td>
            </tr>
            <tr>
                <td class="label">确认密码：</td>
                <td class="input"><input type='password' name="rawPassword2" />
                </td>
            </tr>
            <tr align="left">
                <td align="right">&nbsp;</td>
                <td style="padding: 5px 0 0 0;"><input class="submitButton" onclick='this.form.action="{$contextPath}/admin/chgpwd"' name="Submit" type="submit" value="确定"></td>
            </tr>
        </table>
    </form>
    <script type="text/javascript">
$(document).ready(function(){$.doTimeout(100, function() {
    $(".submitButton").click(function() {
        var $form=$(this.form);
        $form.block();
        $.post(this.form.action, $form.serialize(), function(json) {
            $form.get(0).reset();
            alert(json.message);
        }, "json");
        

        return false;
    });
});});

</script>
    {/if}
</div>
