package cn.app.library.picture.lib.observable;



import java.util.List;

import cn.app.library.picture.lib.entity.LocalMedia;
import cn.app.library.picture.lib.entity.LocalMediaFolder;

/**
 * author：luck
 * project：PictureSelector
 * package：com.luck.picture.lib.observable
 * email：893855882@qq.com
 * data：17/1/16
 */
public interface ObserverListener {
    void observerUpFoldersData(List<LocalMediaFolder> folders);

    void observerUpSelectsData(List<LocalMedia> selectMedias);
}
