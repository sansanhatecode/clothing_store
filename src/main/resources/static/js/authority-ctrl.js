var app = angular.module("myapp", []);
app.controller("authority-ctrl", function($scope, $http, $location) {
	$scope.roles = [];
	$scope.admins = [];
	$scope.allAdmins = [];
	$scope.authorities = [];
	$scope.staff = [];
	$scope.currentPage = 0;
	$scope.pageSize = 5; // Số bản ghi hiển thị trên mỗi trang
	// Hàm để lấy mảng các số trang
    $scope.getPagesArray = function() {
        var pagesArray = [];
        var totalPages = $scope.numberOfPages();
        
        for (var i = 0; i < totalPages; i++) {
            pagesArray.push(i);
        }
        
        return pagesArray;
    };
     // Hàm để chuyển đến trang được chọn
    $scope.goToPage = function(page) {
        $scope.currentPage = page;
    };
	// Thêm tùy chọn "Tất cả" vào mảng roles
	$scope.roles.push({ roleID: -1, rolename: "Tất cả" });

	// Thiết lập giá trị mặc định cho biến selectedRole
	$scope.selectedRole = $scope.roles[0];

	$scope.numberOfPages = function() {
		return Math.ceil($scope.admins.length / $scope.pageSize);
	};

	$scope.getPaginatedAdmins = function() {
		var start = $scope.currentPage * $scope.pageSize;
		var end = start + $scope.pageSize;
		return $scope.admins.slice(start, end);
	};

	$scope.nextPage = function() {
		if ($scope.currentPage < $scope.numberOfPages() - 1) {
			$scope.currentPage++;
		}
	};

	$scope.prevPage = function() {
		if ($scope.currentPage > 0) {
			$scope.currentPage--;
		}
	};

	$scope.initialize = function() {
		$http.get("/admin/roles").then(resp => {
			$scope.roles = resp.data;
			// Thêm tùy chọn "Tất cả" vào mảng roles
			$scope.roles.push({ roleID: -1, rolename: "Tất cả" });

			// Thiết lập giá trị mặc định cho biến selectedRole
			$scope.selectedRole = $scope.roles[3];
		})

		$http.get("/admin/checkadmin").then(resp => {
			$scope.admins = resp.data;
			$scope.allAdmins = resp.data;
		})
		$http.get("/admin/staff").then(resp => {
			$scope.staff = resp.data;
		})
		$http.get("/admin/authorities").then(resp => {
			$scope.authorities = resp.data;
		}).catch(error => {
			$location.path("/unauthorized");
		})

		// Gọi hàm filterUsersByRole để hiển thị tất cả người dùng khi mở trang
		//$scope.filterUsersByRole(); // Bỏ dòng này để không gọi filter khi mở trang
	}

	// Cập nhật danh sách người dùng khi người dùng thay đổi filter
	$scope.filterUsersByRole = function() {
		// Lấy vai trò đã chọn
		var selectedRole = $scope.selectedRole;

		// Nếu người dùng chọn "Tất cả", không gọi API để lấy danh sách người dùng theo vai trò
		if (selectedRole.roleID === -1) {
			$scope.admins = $scope.allAdmins; // Hiển thị tất cả người dùng
		} else {
			// Ngược lại, gọi API để lấy danh sách người dùng theo vai trò
			$http.get('/admin/authorities/filterByRole/' + selectedRole.roleID)
				.then(function(response) {
					// Lấy danh sách người dùng từ response.data
					var users = response.data;

					// Cập nhật danh sách người dùng hiển thị
					$scope.admins = users;
					$scope.currentPage = 0;
				})
				.catch(function(error) {
					console.error('Lỗi khi lấy danh sách người dùng:', error);
				});
		}
	};

	$scope.authority_of = function(acc, role) {
		if ($scope.authorities) {
			return $scope.authorities.find(ur => ur.user.userID == acc.userID && ur.role.roleID == role.roleID);
		}
	}
	$scope.authority_changed = function(acc, role) {
		var authority = $scope.authority_of(acc, role);
		if (authority) { /* đã cấp quyền -> thu hồi quyền */
			$scope.revoke_authority(authority);
		} else { /* ngược lại nếu chưa cấp quyền -> thêm mới */
			authority = { user: acc, role: role };
			$scope.grant_authority(authority);
		}
	}
	 /* Thêm mới authority */
    $scope.grant_authority = function(authority) {
        $http.post(`/admin/authorities`, authority).then(resp => {
            $scope.authorities.push(resp.data)
            alert("Cấp quyền sử dụng thành công");
        }).catch(error => {
            alert("Cấp quyền sử dụng thất bại");
            console.log("Error", error);
        })
    }
    /* Xóa authority */
    $scope.revoke_authority = function(authority) {
        $http.delete(`/admin/authorities/${authority.authoritiesID}`).then(resp => {
            var index = $scope.authorities.findIndex(a => a.authoritiesID == authority.authoritiesID);
            $scope.authorities.splice(index, 1);
            alert("Thu hồi quyền sử dụng thành công");
        }).catch(error => {
            alert("Thu hồi quyền sử dụng thất bại");
            console.log("Error", error);
        })
    }
	
	$scope.grant_staff = function(staff){
		
	};
	
	$scope.revoke_staff = function(staff) {
	
	};

	$scope.initialize();
});
