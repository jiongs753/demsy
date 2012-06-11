{tostring bean=$data prop=$ui.propName pattern=$ui.pattern assign="value"}
{assign var="mode" value=$ui.mode}

{if $mode == "M" || $mode == "E" || $mode == "R" || $mode == "D"}
<input id="fld{$ui.id}" name="{$ui.name}" class="input" value="{$value}" type="{$ui.inputType}" {if $style != null}style="{$style}"{/if} {if $mode=="R"} readonly{/if}{if $mode=="D"} disabled{/if} />
{elseif $mode == "I"}
<input id="fld{$ui.id}" name="{$ui.name}" value="{$value}" type="hidden" />
<span>{tohtml bean=$value}</span>
{elseif $mode == "H"}
<input id="fld{$ui.id}" name="{$ui.name}" value="{$value}" type="hidden" />
{elseif $mode == "S"}
<span>{tohtml bean=$value}</span>
{elseif $mode == "P" && $value != null && $value != ""}
<span>{tohtml bean=$value}</span>
{/if}

