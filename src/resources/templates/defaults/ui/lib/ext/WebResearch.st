<style>
.box tr.title {
	background-color: #75bbcb;
}

.box table.title {
	background-color: #b2dce4;
	font-size: 12px;
	color: #005264;
	text-decoration: none;
	font-weight: bold;
	height: 25px;
	margin: 0 0 6px 0;
}

.box .title .logo {
	background-repeat: no-repeat;
	background-position: left center;
	width: 25px;
}

.box .body {
	padding: 4px 4px 4px 4px;
	border: 0px solid #2996af;
	background-color: #e9e9e9;
	font-size: 12px;
}

.subject {
	padding: 10px 4px 10px 4px;
	background-repeat: repeat-y;
	background-position: right top;
	border: 1px solid #cacaca;
	background-color: #ffffff;
}

.question .desc {
	
}

.question .options {
	margin: 0 0 10px 0px;
	padding: 0 0 0 0;
}

.question .options ul {
	margin: 0px 0 0 0;
	padding: 2px 0 0 0;
}

.question .options ul li span{
	text-align: left;
	font-size: 12px;
}
.question .options ul li td{
	text-valign: top;
}

.buttons {
	text-align: center;
}

.required{
	padding-left: 10px;
	background-image: url({$stylePath}/images/required.gif);
	background-position: center bottom;
	background-repeat: no-repeat;
}
</style>
<form id="form_{$ui.uiid}" method="post" >
<table cellpadding=0 cellspacing=0 border=0 width=100% class="box">
<tr class='title'><td class='title'><table class='title' cellpadding='0' cellspacing='0' border='0' width='100%' v-align='top'><tr>
<td class='begin'>&nbsp;</td>
<td class='logo'>&nbsp;</td>
<td class='name'>{$data.name}</td>
<td class='more'>&nbsp;</td>
<td class='end'>&nbsp;</td>
</tr></table></td></tr>
<tr class='body'><td class='body' colspan='4'>
<div class="subject">
	{foreach from=$data.items item="qnode" key="qidx"}
	<div class="question">
		<div class="desc">{tostring bean=$qnode.obj.name}{if $qnode.obj.mustable == 1}<span class="required">&nbsp;</span>{/if}</div>
		{if $qnode.obj.type == 2}
			<input type="hidden" name="data.[{$qidx}].question.id" value="{$qnode.obj.id}" />
			<textarea style="margin: 2px 2px 2px 10px; width: 400px;" name="data.[{$qidx}].answerText"></textarea>
		{else}
			<div class="options"><ul style="list-style:none;">{foreach from=$qnode.items item="anode" key="aidx"}
				<li class="option">
					<input value="{$anode.obj.id}" id ="option_{$qidx}_{$aidx}"
						{if $qnode.obj.type == 0}
							type="radio" name="data.[{$qidx}].option.id"
						{else}
							type="checkbox" name="data.[{$qidx}_{$aidx}].option.id"
						{/if}
						onclick="
							{if $qnode.obj.type == 0}
							$('.answerTextDiv_{$qidx}').hide();
							$('.answerText_{$qidx}').attr('disabled', true);
							{/if}
							{if $anode.obj.type == 1}
							var $parent=$(this).parent();
							if(this.checked){
								$('.answerTextDiv_{$qidx}', $parent).show();
								$('.answerText_{$qidx}', $parent).attr('disabled', false);
							}else{
								$('.answerTextDiv_{$qidx}', $parent).hide();
								$('.answerText_{$qidx}', $parent).attr('disabled', true);
							}
							{/if}
							"
					/>
					<label for="option_{$qidx}_{$aidx}">{tostring bean=$anode.obj.name}</label>
					{if $anode.obj.type == 1}
					<div class="answerTextDiv_{$qidx}" style="display:none;padding:2px 2px 2px 30px;">
						<input class="answerText_{$qidx}" type="text" disabled="disabled" name="data.[{if $qnode.obj.type == 0}{$qidx}{else}{$qidx}_{$aidx}{/if}].answerText" style="width: 300px;" />
					</div>
					{/if}
				</li>{/foreach}
			</ul></div>
		{/if}
	</div>
	{/foreach}
	<div class="buttons">
        <input type="submit" class="submitButton" value="提交" />
        {tostring assign="view" bean=$data prop="view"}
        {if $view == 1}
        {import file="../a.st" href=$data.href target=$item.target name="查看"}
        {/if}
	</div>
</div>
</td></tr></table></form>
</form>
<script type="text/javascript">
$(function() {
	var $form=$("#form_{$ui.uiid}");
	$(".submitButton", $form).click(function() {
		$form.block(); 
        var form=$form.get(0);
		$.post("{$saveUrl}", $form.serialize(), function(json) {
            alert(json.message);
            if(json.success){
                form.reset();
            }
		}, "json");

		return false;
	});
});
</script>