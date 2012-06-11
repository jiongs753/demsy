{if $user == null}
<script type="text/javascript">
	window.document.location.href="/";
	alert("你尚未登录或登录已经过期! 请先登录.");
</script>
{/if}
<div class="t">{$block.name}</div>
<div class="d">
    <table>
        <tr>
            <td class="img" width=100 height=100>
            {if $user.image != null}
                {import file="a.st" href=$data.href target=$data.target title=$data.title img=$user.image} 
            {elseif $user.logo != null}
                {import file="a.st" href=$data.href target=$data.target title=$data.title img=$user.logo}
            {else}
                <div class="defaultImg"></div>
            {/if}
            </td>
            <td class="inf">
	            <div><span>名称：</span> {tostring bean=$user prop="name"}</div> 
	            <div><span>帐号：</span> {tostring bean=$user prop="username"}</div>
	            <div><span>等级：</span> {tostring bean=$user prop="grade"}</div>
            </td>
        </tr>
    </table>
</div>
<div class="b"></div>
