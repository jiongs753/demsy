{tostring bean=$data prop=$ui.propName assign="value"}
{assign var="mode" value=$ui.mode}

{if $mode == "M" || $mode == "E"}
<div class="Combobox upload" {if $style != null}style="{$style}"{/if} uploadUrl="{$ui.uploadUrl}" acceptType="{tostring bean=$ui.uploadType}" stylePath="{$stylePath}">
    <input id="{$ui.uiid}_{$idPost}" {if $style != null}style="{$style}"{/if} class="input" type="text" name="{$ui.name}" value="{$value}" />
    <div class="logo"><input class="uploadify" id="{$ui.uiid}_{$idPost}File" disabled="true" style="display:none;" name="upload" type="file" /></div>
    <div class="progress" id="{$ui.uiid}_{$idPost}FileProgress"></div>
</div>
{elseif $mode == "R" || $mode == "D"}
<input id="{$ui.uiid}" name="{$ui.name}" value="{$value}"{if $mode=="R"} readonly{/if}{if $mode=="D"} disabled{/if} />
{elseif $mode == "I"}
<input id="{$ui.uiid}" name="{$ui.name}" value="{$value}" type="hidden" />
<span><a target="download" href="{$contextPath}{tostring bean=$value}">{tostring bean=$value}</a></span>
{elseif $mode == "H"}
<input id="{$ui.uiid}" name="{$ui.name}" value="{$value}" type="hidden" />
{elseif $mode == "S"}
<span><a target="download" href="{$contextPath}{tostring bean=$value}">{tostring bean=$value}</a></span>
{elseif $mode == "P" && $value != null && $value != ""}
<span><a target="download" href="{$contextPath}{tostring bean=$value}">{tostring bean=$value}</a></span>
{/if}