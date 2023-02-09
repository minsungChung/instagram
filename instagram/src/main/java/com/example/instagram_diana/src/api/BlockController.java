package com.example.instagram_diana.src.api;

import com.example.instagram_diana.config.BaseException;
import com.example.instagram_diana.config.BaseResponse;
import com.example.instagram_diana.src.service.BlockService;
import com.example.instagram_diana.src.user.UserService;
import com.example.instagram_diana.src.utils.JwtService;
import org.springframework.web.bind.annotation.*;

import static com.example.instagram_diana.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/users")
public class BlockController {

    private final UserService userService;
    private final BlockService blockService;

    private final JwtService jwtService;

    public BlockController(UserService userService, BlockService blockService, JwtService jwtService) {
        this.userService = userService;
        this.blockService = blockService;
        this.jwtService = jwtService;
    }

    @PostMapping("/blocks/{toUserId}")
    public BaseResponse<?> block(@PathVariable("toUserId") long toUserId){

        try{

            if (!userService.checkUserExist(toUserId)){
                return new BaseResponse<>(USER_ID_NOT_EXIST);
            }


            Long fromUserId = jwtService.getUserIdx();

            if(toUserId==fromUserId){
                return new BaseResponse<>(POST_CANNOT_SELF);
            }

            if(blockService.checkBlock(fromUserId,toUserId)==1){
                return new BaseResponse<>(USER_ID_ALREADY_BLOCK);
            }else{
                blockService.block(fromUserId,toUserId);
                return new BaseResponse<>("차단 요청 성공");
            }

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    @PostMapping("/unblocks/{toUserId}")
    public BaseResponse<?> unBlock(@PathVariable("toUserId") long toUserId){

        try{

            if (!userService.checkUserExist(toUserId)){
                return new BaseResponse<>(USER_ID_NOT_EXIST);
            }

            Long fromUserId = jwtService.getUserIdx();

            if(toUserId==fromUserId){
                return new BaseResponse<>(POST_CANNOT_SELF);
            }

            if(blockService.checkBlock(fromUserId,toUserId)!=1){
                return new BaseResponse<>(USER_ID_ALREADY_UNBLOCK);
            }else{
                blockService.unblock(fromUserId,toUserId);
                return new BaseResponse<>("차단 해제요청 성공");
            }


        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    @GetMapping("block-states/{toUserId}")
    public BaseResponse<?> blockState(@PathVariable("toUserId") long toUserId){
        int res=-1;
        if (userService.checkUserExist(toUserId)){
            return new BaseResponse<>("차단 유저입니다.",1);
        }else{
            return new BaseResponse<>("차단하지않은 유저입니다.",0);
        }
    }


}
