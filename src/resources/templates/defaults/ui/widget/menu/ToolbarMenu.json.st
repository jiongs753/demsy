[""{if $data != null && $data.size > 0}{foreach from=$data.children item="item"},{if $item.type == "-"}"-"{else}{ 
  "id":"{$item.id}", "text":"{$item.name}", "icon":"{tostring bean=$item prop="icon"}", "url":"{tostring bean=$item prop="url"}", "target":"{tostring bean=$item prop="target"}", "title":"{tostring bean=$item prop="desc"}", "type":"{$item.type}", "mode":"{tostring bean=$item prop="mode"}", "moduleID":"{tostring bean=$item prop="moduleID"}", "masterModuleID":"{tostring bean=$item prop="masterModuleID"}" ,"params":"{tostring bean=$item prop="params"}"
 {if $item.size > 0},"items": {import file="ToolbarMenu.json.st" data=$item}{/if}}{/if}{/foreach}{/if}
]
