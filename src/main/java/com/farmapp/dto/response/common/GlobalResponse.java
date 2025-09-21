package com.farmapp.dto.response.common;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GlobalResponse<T> {
    private int status;
    private String message;
    private T data;

    public static <T> GlobalResponseBuilder<T> builder() {
        return new GlobalResponseBuilder<>();
    }

    public static <T> GlobalResponse<T> success(T data) {
        return new GlobalResponse<>(200, "Success", data);
    }

    public static <T> GlobalResponse<T> error(String message, int statusCode) {
        return new GlobalResponse<>(statusCode, message, null);
    }
    public static <T> GlobalResponse<T> success(T data, String message) {
        return new GlobalResponse<>(200, message, data);
    }

    public static class GlobalResponseBuilder<T> {
        private int status;
        private String message;
        private T data;

        public GlobalResponseBuilder<T> status(int status) {
            this.status = status;
            return this;
        }

        public GlobalResponseBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public GlobalResponseBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public GlobalResponse<T> build() {
            return new GlobalResponse<>(status, message, data);
        }
    }
}

