package com.lechatong.beakhub.Tools;

import com.lechatong.beakhub.WebService.BeakHubService;

public interface ServiceCallback<T> {
    void success(T value);

    void error(Throwable throwable);
}
