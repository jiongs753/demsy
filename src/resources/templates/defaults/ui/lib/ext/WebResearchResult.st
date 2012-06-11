<style>
.box {
	border: 4px solid #8acb64;
	background-color: #e0fad2;
	padding: 5px 5px 5px 5px;
}

.box span {
	font-size: 12px;
}

.box .subject {
	font-weight: 900;
	font-size: 14px;
	padding: 10px;
}

table.question {
	border: 1px solid #8acb64;
	border-bottom: 0px solid #8acb64;
	background-color: #ffffff;
	font-weight: 900;
	padding: 2 2 2 2;
}

.subject_result {
	padding: 10px;
}

.subject_result .result {
	color: red;
	font-weight: 900;
}

table.question .title {
	background-color: #8acb64;
	height: 25px;
}

table.question .title td {
	border-bottom: 1px solid #cccccc;
	padding-left: 10px;
}

table.question .header {
	background-color: #fffff0;
	height: 25px;
}

table.question .name {
	border-right: 1px solid #cccccc;
	border-bottom: 1px solid #cccccc;
	height: 22px;
	padding: 5px 5px 5px 10px;
}

table.question .percentage {
	border-right: 1px solid #cccccc;
	border-bottom: 1px solid #cccccc;
	width: 150px;
}

table.question .value {
	border-bottom: 1px solid #cccccc;
	width: 100px;
	padding: 0 10px 0 10px;
}

.option {
	font-weight: 300;
}

.option .percentage_text{
	border-bottom: 1px solid #cccccc;
	width: 50px;
	padding: 0 10px 0 10px;
}
.option .percentage {
	width: 100px;
}
.option .percentage .bar {
	position: relative;
	float: left;
	width: 200px;
	border: 0px solid #8acb64;
	background-color: #cccccc;
	height: 10px;
	margin-left:5px;
	right: 5px;
	text-align: left;
	font-size: 11px;
}

.option .percentage .valuebar {
	background-color: red;
	height: 10px;
	align: left;
}
</style>
<table cellpadding=0 cellspacing=0 border=0 width=100% class="box">
	<tr class='subject'><td class='subject'>{$data.name}</td></tr>
	<tr class='subject_result'><td class='subject_result'><span>共投了 <span class="result">{tostring bean=$data prop="resultSumValue"}</span> 票</span></td></tr>
	{foreach from=$data.items item="qnode" key="qidx"}
	<tr class='question'>
		<td class='question'><table class="question" cellpadding=0 cellspacing=0 border=0 width=100%>
			<tr class='title'><td class='title' colspan=4>{$qnode.obj.name}</td></tr>
			<tr class='header'>
				<td class='name' align="center" style="font-weight: 900;">选项</td>
				<td class='percentage' align="center" style="font-weight: 900;" colspan=2>比例</td>
				<td class='value' align="center" style="font-weight: 900;">票数</td>
			</tr>
			{foreach from=$qnode.items item="anode" key="aidx"}
			<tr class='option'>
				<td class='name'>{$anode.name}</td>
				<td class='percentage_text' align="right">{tostring bean=$anode prop="resultPercentage"}%</td>
				<td class='percentage' align="left" ><div class="bar"><div class="valuebar" style="width:{tostring bean=$anode prop="resultPercentage"}%;"></div></div></td>
				<td class='value' align="right">{tostring bean=$anode prop="resultSumValue"}</td>
			</tr>
		{/foreach}
		</table></td>
	</tr>
	{/foreach}
</table>
