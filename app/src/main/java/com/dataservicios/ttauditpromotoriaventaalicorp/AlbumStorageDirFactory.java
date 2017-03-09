package com.dataservicios.ttauditpromotoriaventaalicorp;

import java.io.File;

public abstract class AlbumStorageDirFactory {
	public abstract File getAlbumStorageDir(String albumName);
}
