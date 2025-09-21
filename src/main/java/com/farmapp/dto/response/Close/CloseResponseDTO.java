    package com.farmapp.dto.response.Close;

    import com.fasterxml.jackson.annotation.JsonFormat;
    import lombok.*;

    import java.time.LocalDate;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public class CloseResponseDTO {
        private String id;
        private Integer farmerId;
        private String farmerName;
        private Integer seasonId;
        private String seasonName;
        private Integer productId;
        private String productName;
        private Float totalQuantity;
        private Integer price;
        private String note;
        @JsonFormat(pattern = "dd-MM-yyyy")
        private LocalDate createdAt;
        private String closeImageUrl;
    }
