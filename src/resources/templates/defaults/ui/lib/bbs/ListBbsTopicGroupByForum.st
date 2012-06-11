<div class="t"></div>
<ul class="d">
	{foreach from=$data.items item="cata" key="idx"}
	<li class="i"><table class="i"><tr>
		<td class="img" align="center" valign="center">
			{import file="../a.st" href=$cata.href target=$cata.target title=$cata.title img=$cata.img}
		</td>
		<td class="it" valign="top">
			<div class="tt">{import file="../a.st" href=$cata.href target=$cata.target title=$cata.title name=$cata.name}</div>
			<div class="tdesc">{tostring bean=$cata.obj prop="desc"}</div>
		</td>
		<td class="i_2" valign="top">
			<table width="100%" class="i_2">
				<tr><th class="ititle">标题</th><th class="iuser">作者</th><th class="idate">时间</th></tr>{foreach from=$cata.items item="item" key="idx"}
				<tr>
					<td class="ititle">{import file="../a.st" href=$item.href target=$item.target title=$item.title name=$item.name}</td>
					<td class="iuser">{tostring bean=$item.obj prop="createdBy"}</td>
					<td class="idate">{tostring bean=$item.obj prop="created" pattern="yyyy-MM-dd HH:mm"}</td>
				</tr>{/foreach}
			</table>
		</td>
		</tr></table>
	</li>
	{/foreach}
</ul>
<div class="b"></div>
