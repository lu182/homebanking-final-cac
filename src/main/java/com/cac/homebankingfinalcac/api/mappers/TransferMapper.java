package com.cac.homebankingfinalcac.api.mappers;

import com.cac.homebankingfinalcac.api.dtos.TransferDTO;
import com.cac.homebankingfinalcac.domain.models.TransferEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TransferMapper {

    public TransferEntity dtoToTransfer(TransferDTO dto){
        TransferEntity transfer = new TransferEntity();
        transfer.setOriginAccount(dto.getOriginAccount());
        transfer.setTargetAccount(dto.getTargetAccount());
        transfer.setTransferDate(dto.getTransferDate());
        transfer.setAmountTransfer(dto.getAmountTransfer());
        return transfer;
    }

    public TransferDTO transferToDto(TransferEntity transfer){
        TransferDTO dto = new TransferDTO();
        dto.setIdTransfer(transfer.getIdTransfer());
        dto.setOriginAccount(transfer.getOriginAccount());
        dto.setTargetAccount(transfer.getTargetAccount());
        dto.setTransferDate(transfer.getTransferDate());
        dto.setAmountTransfer(transfer.getAmountTransfer());
        return dto;
    }
}