{prop bean=$data name=$ui.propName assign="value"}
{key bean=$value assign="fieldValue"}
{assign var="mode" value=$ui.mode}

{if $mode == "M" || $mode == "E" || $mode == "R" || $mode == "D"}
<div style="white-space: nowrap;">{import file="../../../common/radios.st" nodes=$ui.optionNode.children name=$ui.name}</div>
{elseif $mode == "I"}
<input id="fld{$ui.id}" name="{$ui.name}" value="{$fieldValue}" type="hidden" />
<span>{import file="../../../common/optionstr.st" nodes=$ui.optionNode.children}</span>
{elseif $mode == "H"}
<input id="fld{$ui.id}" name="{$ui.name}" value="{$fieldValue}" type="hidden" />
{elseif $mode == "S" || $mode == "P"}
<span>{import file="../../../common/optionstr.st" nodes=$ui.optionNode.children}</span>
{/if}
