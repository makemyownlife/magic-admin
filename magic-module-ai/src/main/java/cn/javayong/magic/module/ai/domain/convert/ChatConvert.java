package cn.javayong.magic.module.ai.domain.convert;

import cn.javayong.magic.module.ai.adapter.command.OpenAIChatReqCommand;
import cn.javayong.magic.module.ai.domain.vo.OpenAIChatReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ChatConvert {

    ChatConvert INSTANCE = Mappers.getMapper(ChatConvert.class);

    OpenAIChatReqCommand convert(OpenAIChatReqVO openAIChatReqVO);

}
