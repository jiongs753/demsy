{assign var="mode" value=$ui.mode}
<input id="fld{$ui.id}" name="{$ui.name}" value="{$value}" type="hidden" />
<style>
.ResearchQuestion {
	border: 1px solid #79b7e7;
	BORDER-COLLAPSE: collapse;
	width: 600px;
	position: relative;
}

.ResearchQuestion .qItem{
	margin-bottom: 5px;
	width: 100%;
}

.ResearchQuestion .qItem th {
	padding: 3px;
	height: 22px;
	background: #dfeffc;
	border-top: 1px solid #c5dbec;
	border-bottom: 1px solid #c5dbec;
}

.ResearchQuestion td {
	padding: 3px;
	height: 22px;
}

.ResearchQuestion .qId, .ResearchQuestion .oId{
	display: none;
}
.ResearchQuestion .qName input{
	width: 350px;
	height: 20px;
	border: 1px solid #c5dbec;
}

.ResearchQuestion .oName input{
	margin-left: 15px;
	width: 420px;
	height: 20px;
	border: 1px solid #c5dbec;
}

.ResearchQuestion .qType select,.ResearchQuestion .qMust select,.ResearchQuestion .oType select{
	width: 80px;
	height: 22px;
	border: 1px solid #c5dbec;
}

.ResearchQuestion .qOp,.ResearchQuestion .oOp {
	width: 80px;
	text-align: center;
}

.OptionDialog,.QustionDialog {
	display: none;
}
</style>
<div class="ResearchQuestion">
	<div class="qList">
	{foreach from=$data.questions item="q"}
	<table class="qItem">
		<tr>
			<th class="qId"><input type="hidden" value="{$q.id}" /></th>
			<th class="qName"><input type="text" value="{$q.name}" /></th>
			<th class="qMust"><select>
					<option {if $q.mustable == 0}selected{/if} value="0">可选</option>
					<option {if $q.mustable == 1}selected{/if} value="1">必填</option>
			</select></th>
			<th class="qType"><select>
					<option {if $q.type == 0}selected{/if} value="0">单选</option>
					<option {if $q.type == 1}selected{/if} value="1">多选</option>
					<option {if $q.type == 2}selected{/if} value="2">问卷</option>
			</select></th>
			<th class="qOp">
				<input class="btnDelQuestion" type="submit" title="删除问题" alt="删除问题" value="×" onclick="return false;" />
				<input class="btnAddOption" type="submit" title="添加选项" alt="添加选项" value="+" onclick="return false;" />
			</th>
		</tr>
		{foreach from=$q.options item="o"}
		<tr class="oItem">
			<td class="oId"><input type="hidden" value="{$o.id}" /></td>
			<td class="oName" colspan=2><input type="text" value="{$o.name}" /></td>
			<td class="oType"><select>
				<option {if $o.type == 0}selected{/if} value="0">选择</option>
				<option {if $o.type == 1}selected{/if} value="1">输入</option>
			</select></td>
			<td class="oOp"><input class="btnDelOption" type="submit" title="删除选项" alt="删除选项" value="×" onclick="return false;" /></td>
		</tr>
		{/foreach}
	</table>
	{/foreach} {if $mode == "M" || $mode == "E"}
	</div>
	<div class="qTemplate" style="display:none;">
		<table class="qItem">
			<tr>
				<th class="qId"><input type="hidden" value="" /></th>
				<th class="qName"><input type="text" value="" /></th>
				<th class="qMust"><select>
						<option value="0">可选</option>
						<option value="1">必填</option>
				</select></th>
				<th class="qType"><select>
						<option value="0">单选</option>
						<option value="1">多选</option>
						<option value="2">问卷</option>
				</select></th>
				<th class="qOp">
					<input class="btnDelQuestion" type="submit" title="删除问题" alt="删除问题" value="×" onclick="return false;" />
					<input class="btnAddOption" type="submit" title="添加选项" alt="添加选项" value="+" onclick="return false;" />
				</th>
			</tr>
		</table>
	</div>
	<table class="oTemplate" style="display:none;">
		<tr class="oItem">
			<td class="oId"><input type="hidden" value="" /></td>
			<td class="oName" colspan=2><input type="text" value="" /></td>
			<td class="oType"><select>
				<option value="0">选择</option>
				<option value="1">输入</option>
			</select></td>
			<td class="oOp"><input class="btnDelOption" type="submit" title="删除选项" alt="删除选项" value="×" onclick="return false;" /></td>
		</tr>
	</table>
	<div style="text-align: right;"><input class="btnAddQuestion" type="submit" value="添加问题(+)" onclick="return false;" /></div>
	{/if}
</div>
<script>
$(function(){
	var $qBox = $(".ResearchQuestion");
	var addQuestion=function(){
		var $qList = $(".qList", $qBox);
		var $qItem =$($(".qTemplate",$qBox).html()).appendTo($qList);
		$(".btnAddOption",$qItem).click(addOption);
		$(".btnDelQuestion",$qItem).click(delQuestion);
	}
	var delQuestion=function(){
		var $qItem=$(this).parent().parent().parent().parent();
		$qItem.remove();
	};
	var addOption=function(){
		var $qItem=$(this).parent().parent().parent().parent();
		var $oItem=$($(".oTemplate",$qBox).html()).appendTo($qItem);
		$(".btnDelOption",$oItem).click(delOption);
	};
	var delOption=function(){
		var $oItem=$(this).parent().parent();
		$oItem.remove();
	};
	$(".btnAddQuestion",$qBox).click(addQuestion);
	$(".btnDelQuestion",$qBox).click(delQuestion);
	$(".btnAddOption",$qBox).click(addOption);
	$(".btnDelOption",$qBox).click(delOption);
	
	$(".submitButton", $($(".btnAddQuestion",$qBox).get(0).form)).click(function(){
		var $qList = $(".qList", $qBox);
		var jsonQuestions=[];
		$(".qItem", $qList).each(function(){
			var $qItem=$(this);
			var qId=$("input", $(".qId",$qItem)).val();
			var qName=$("input", $(".qName",$qItem)).val();
			var qType=$("select", $(".qType",$qItem)).val();
			var qMust=$("select", $(".qMust",$qItem)).val();

			var jsonQ={
			};
			var jsonOptions=[];
			jsonQ["id"]=qId;
			jsonQ["name"]=qName;
			jsonQ["type"]=qType;
			jsonQ["mustable"]=qMust;
			jsonQ["options"]=jsonOptions;
			
			$(".oItem",$qItem).each(function(){
				var $oItem=$(this);
				var oId=$("input", $(".oId",$oItem)).val();
				var oName=$("input", $(".oName",$oItem)).val();
				var oType=$("select", $(".oType",$oItem)).val();
				
				var jsonO={
				};
				jsonO["id"]=oId;
				jsonO["name"]=oName;
				jsonO["type"]=oType;
				jsonOptions[jsonOptions.length]=jsonO;
			});
			
			jsonQuestions[jsonQuestions.length]=jsonQ;
		});
		
		$("#fld{$ui.id}").val($.toJSON(jsonQuestions));
	});
})
</script>